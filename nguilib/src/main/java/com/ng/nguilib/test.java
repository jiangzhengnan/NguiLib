//
//  SpeechRecognizerAnimator.swift
//  Component
//
//  Created by Tank on 2019/12/18.
//  Copyright © 2019 Webull. All rights reserved.
//
/**
 * import Configuration
 *
 * import YYText
 *
 * public class SpeechRecognizerAnimator: SSBaseView {
 * /// 直线条数
 * private let waveCount = 24
 *
 * private var displayLink: CADisplayLink?
 * private var volumn: CGFloat = 0.5
 *
 * private var dynamicAttenuations = [DynamicAttenuationModel]()
 *
 * /// 动画开始时间
 * private var startTime = CACurrentMediaTime()
 *
 * private let sliderView = UISlider()
 *
 * public override func setup() {
 *         super.setup()
 *
 *         self.backgroundColor = Color.nc102.color
 *
 *         self.sliderView.ss.customize { (view) in
 *         view.minimumValue = 0
 *         view.maximumValue = 1
 *         view.thumbTintColor = Color.nc401.color
 *         view.maximumTrackTintColor = Color.nc101.color
 *         view.minimumTrackTintColor = Color.nc401.color
 *         view.value = Float(self.volumn)
 *         view.addTarget(self, action: #selector(slider), for: .valueChanged)
 *
 *         self.addSubview(view)
 *
 *         view.snp.makeConstraints { (make) in
 *         make.bottom.equalToSuperview().offset(-32)
 *         make.centerX.equalToSuperview()
 *         make.size.equalTo(CGSize(width: 200, height: 50))
 *         }
 *         }
 *
 *         self.start()
 *         }
 *
 * @objc func slider() {
 *         self.volumn = CGFloat(self.sliderView.value)
 *         }
 *
 * private func start() {
 *
 *         self.displayLink = CADisplayLink(target: YYTextWeakProxy(target: self), selector: #selector(animationLoop))
 *         self.displayLink?.add(to: RunLoop.main, forMode: RunLoop.Mode.common)
 *
 *         self.startTime = CACurrentMediaTime()
 *         }
 *
 * private func stop() {
 *
 *         self.displayLink?.isPaused = true
 *         self.displayLink?.invalidate()
 *         self.displayLink = nil
 *         }
 *
 * @objc private func animationLoop() {
 *         /// 随机衰减波动点
 *         // 剔除已经执行完的点
 *         self.dynamicAttenuations = self.dynamicAttenuations.filter({ $0.startTime + $0.totalDuration > CACurrentMediaTime() })
 *
 *         /// 如果为空则重新生成点
 *         if self.dynamicAttenuations.count <= 1 {
 *         /// 随机生成2-4个点
 *         for _ in 0..<Int.random(in: 1...3) {
 *         self.dynamicAttenuations.append(DynamicAttenuationModel())
 *         }
 *         }
 *
 *         self.setNeedsDisplay()
 *         }
 *
 *         func setVolum(_ volum: CGFloat) {
 *         self.volumn = volumn
 *         }
 *
 *         deinit {
 *         self.stop()
 *         }
 *
 * public override func draw(_ rect: CGRect) {
 *
 *         guard let context = UIGraphicsGetCurrentContext(),
 *         self.bounds.width > 10, self.bounds.height > 10 else { return }
 *
 *         let maxDotWidth: CGFloat = 4
 *         let dotHorSpacing: CGFloat = 2
 *
 *         context.saveGState()
 *
 *         defer {
 *         context.restoreGState()
 *         }
 *
 *         let width = self.bounds.width
 *
 *         let color = Color.nc401.color.withAlphaComponent(0.6)
 *
 *         for index in 0..<waveCount {
 *         // 前面的点大，后面的点小
 *         let dotWidth = maxDotWidth * (0.8 + CGFloat(index) * 0.2 / CGFloat(waveCount))
 *
 *         let baseY = 100 + (dotWidth - 2) * CGFloat(index)
 *
 *         // 立体偏移
 *         let startXOffset = (CGFloat(waveCount - index)/CGFloat(waveCount)) * (width / 20)
 *         for x in stride(from: dotWidth + startXOffset - 50, to: width, by: dotWidth + dotHorSpacing) {
 *
 *         let y = baseY + self.equation(x: x, index: index)
 *
 *         context.setFillColor(color.cgColor)
 *
 *         // X偏移量，透视效果
 *         let xOffset: CGFloat = 0// maxDotWidth * CGFloat(waveCount/2 - index)/CGFloat(waveCount/2)
 *         // 圆中心点
 *         let center = CGPoint(x: x + xOffset, y: y)
 *
 *         context.addArc(center: center, radius: dotWidth/2, startAngle: 0, endAngle: CGFloat.pi * 2, clockwise: true)
 *         context.fillPath()
 *         }
 *         }
 *
 *         let locations: [CGFloat] = [0.0, 1.0]
 *         if let gradient = CGGradient(
 *         colorsSpace: CGColorSpaceCreateDeviceRGB(),
 *         colors: [
 *         Color.nc102.color.withAlphaComponent(0).cgColor,
 *         Color.nc102.color.withAlphaComponent(1).cgColor
 *         ] as CFArray,
 *         locations: locations
 *         ) {
 *         context.drawRadialGradient(gradient,
 *         startCenter: self.center,
 *         startRadius: 0,
 *         endCenter: self.center,
 *         endRadius: self.width * 0.66,
 *         options: .drawsBeforeStartLocation)
 *         }
 *         }
 *
 *         /// 圆圈位置计算方程
 *         func equation(x: CGFloat, index: Int) -> CGFloat {
 *         let height = self.bounds.height
 *
 *         let width = self.bounds.width
 *
 *         let currentTime = CACurrentMediaTime()
 *         let elapsedTime = CGFloat(currentTime - self.startTime)
 *
 *         // 一个周期
 *         let animationProcess: CGFloat = elapsedTime / 2 - CGFloat(Int(elapsedTime / 2))
 *
 *         // 正弦的最大高度
 *         let periodHeight = height / 40
 *         // 一个正弦周期宽度
 *         let periodWidth = width / 2
 *         let xProcess = x / periodWidth
 *         let radius = (CGFloat.pi * 2) * (xProcess - CGFloat(Int(xProcess)) - animationProcess - CGFloat(index) * 0.02)
 *
 *         // 正弦结果
 *         let sinResult: CGFloat = sin(radius) * periodHeight * self.volumn
 *
 *         // 波动衰减修正
 *         /// 波动最大振幅
 *         let maxAmplitude = self.volumn * height / 3
 *         var attenuationOffset = CGFloat(0)
 *         for model in self.dynamicAttenuations {
 *         // 衰减进度(线性衰减)
 *         let process: CGFloat
 *         if currentTime - model.startTime - model.upDuration < 0, model.upDuration > 0 {
 *         process = CGFloat((currentTime - model.startTime)/model.upDuration)
 *         } else if currentTime - model.startTime - model.totalDuration < 0, model.downDuration > 0 {
 *         process = 1 - CGFloat((currentTime - model.startTime - model.upDuration)/model.downDuration)
 *         } else {
 *         continue
 *         }
 *
 *         // 当前点(0...1)
 *         let currentPoint = CGPoint(x: x/width, y: CGFloat(index)/CGFloat(self.waveCount))
 *         // 距离中心点距离
 *         let distance = abs(sqrt(pow(model.center.x - currentPoint.x, 2) + pow(model.center.y - currentPoint.y, 2)))
 *
 *         // 范围衰减，中心最大
 *         let distanceProcess = min(max((model.range - distance)/model.range, 0), 1)
 *
 *         attenuationOffset += maxAmplitude * process * distanceProcess * model.amplitude
 *         }
 *
 *         return sinResult + attenuationOffset
 *         }
 *         }
 *
 * /// 动态衰减
 *         struct DynamicAttenuationModel {
 *         /// 衰减开始时间
 *         let startTime: TimeInterval
 *         /// 衰减持续时间
 *         let upDuration: TimeInterval
 *         /// 衰减持续时间
 *         let downDuration: TimeInterval
 *         /// 衰减中心点(0-1)
 *         let center: CGPoint
 *         /// 波及范围(0-1)
 *         let range: CGFloat
 *         /// 波动振幅
 *         let amplitude: CGFloat
 *
 *         var totalDuration: TimeInterval {
 *         return self.upDuration + self.downDuration
 *         }
 *
 *         /// 随机生成
 *         init() {
 *         self.startTime = CACurrentMediaTime()
 *         self.upDuration = TimeInterval.random(in: 0.1...0.4)
 *         self.downDuration = TimeInterval.random(in: 0.2...0.8)
 *         self.center = CGPoint(x: CGFloat.random(in: 0...1), y: CGFloat.random(in: 0...1))
 *         self.range = CGFloat.random(in: 0.4...0.8)
 *         self.amplitude = CGFloat.random(in: -1...1)
 *         }
 *         }
 */