package com.ng.ui.show.view

import android.graphics.Color
import android.view.View
import com.ng.nguilib.CylinderView
import com.ng.ui.R
import kotlinx.android.synthetic.main.fragment_cd.*
import java.util.*

/**
 * 描述:
 * @author Jzn
 * @date 2020-06-12
 */
class CdFragment: BaseFragment() {

    override fun initViewsAndEvents(v: View) {


        clv.setData(arrayListOf(
                CylinderView.Entry(30f,  Color.parseColor("#bbdefb")),
                CylinderView.Entry(40f,  Color.parseColor("#90caf9")),
                CylinderView.Entry(50f,  Color.parseColor("#82b1ff")),
                CylinderView.Entry(60f,  Color.parseColor("#448aff")),
                CylinderView.Entry(70f,  Color.parseColor("#2979ff")),
                CylinderView.Entry(110f, Color.parseColor("#2962ff"))
        ))

        btn1.setOnClickListener {
            clv.startUpAllAnim()
        }
        btn2.setOnClickListener {
            clv.startSingleAnim()
        }
        btn3.setOnClickListener {
            clv.startChangeAnim()
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_cd

    /**
     * 获取十六进制的颜色代码.例如  "#5A6677"
     * 分别取R、G、B的随机值，然后加起来即可
     *／
     * @return String
     */
    fun getRandColor(): String {
        var R: String
        var G: String
        var B: String
        val random = Random()
        R = Integer.toHexString(random.nextInt(256)).toUpperCase()
        G = Integer.toHexString(random.nextInt(256)).toUpperCase()
        B = Integer.toHexString(random.nextInt(256)).toUpperCase()

        R = if (R.length == 1) "0$R" else R
        G = if (G.length == 1) "0$G" else G
        B = if (B.length == 1) "0$B" else B

        return "#$R$G$B"
    }

}