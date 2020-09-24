# NguiLib
English | [简体中文](./README.zh-CN.md) |

适用于Android的*漂亮+实用+简单风格*自定义视图和布局集合，最小sdk版本兼容到14，欢迎rp
<br/>
Jcenter地址: 
<a href="https://bintray.com/jiangzhengnan/NguiLib/NguiLib">https://bintray.com/jiangzhengnan/NguiLib/NguiLib</a><br />

用法
-------
gradle:
```grovvy
repositories {
    jcenter()
}

...

dependencies {
    api 'ng.ngui.ngbase:nguilib:0.0.12'
}
```
<br/>
直接复制在gradle中引入就可以了
<br/>

<h3>布局组件</h3>
1.ZoonLayout. 基于LinearLayout实现的可联动伸缩布局组件.<br />
<img src="https://github.com/jiangzhengnan/NguiLib/blob/master/app/src/main/res/raw/zoom_layout.gif" />

<h3>视图组件</h3>
1.CentralTractionButton. 高仿腾讯QQ实现的tab手势触摸反馈tab按钮   <br />
Information: <a href="https://blog.csdn.net/qq_22770457/article/details/78630695">腾讯QQ的Tab按钮动画效果完美实现</a><br />
<img src="https://github.com/jiangzhengnan/NguiLib/blob/master/app/src/main/res/raw/ctb_show.gif" />
2.EcgView. 各种状态的心电图 <br />
Information: <a href="https://blog.csdn.net/qq_22770457/article/details/90679481">ECG心电图的绘制实现</a><br />
<img src="https://github.com/jiangzhengnan/NguiLib/blob/master/app/src/main/res/raw/ecg_show.gif" />
3.PolygonLoadView. 酷酷的加载view<br />
Inspiration : <a href="https://dribbble.com/shots/5878367-Loaders">https://dribbble.com/shots/5878367-Loaders</a><br />
<img src="https://github.com/jiangzhengnan/NguiLib/blob/master/app/src/main/res/raw/pl_show.gif" /> 
4.ArrowInteractionView. 箭头交互动画<br />
Inspiration : <a href="https://dribbble.com/shots/6201452-Arrow-micro-interaction">https://dribbble.com/shots/6201452-Arrow-micro-interaction</a><br />
<img src="https://github.com/jiangzhengnan/NguiLib/blob/master/app/src/main/res/raw/ai_show.gif" /> 
5.PointLoadingView. 点沉降加载view<br />
<img src="https://github.com/jiangzhengnan/NguiLib/blob/master/app/src/main/res/raw/ptl_show.gif" /> 
6.CylinderView. 实现3d动画效果的圆饼图<br />
Inspiration : <a href="https://dribbble.com/shots/7077455-Spending-analytics">https://dribbble.com/shots/7077455-Spending-analytics</a><br />
<img src="https://github.com/jiangzhengnan/NguiLib/blob/master/app/src/main/res/raw/cd_show.gif" /> 
7.ToggleView. 矢量切换动画<br />
Inspiration : <a href="https://dribbble.com/shots/9834681--Toggle">https://dribbble.com/shots/9834681--Toggle</a><br />
<img src="https://github.com/jiangzhengnan/NguiLib/blob/master/app/src/main/res/raw/tg_show.gif" /> 
 

## License

    Copyright 2019, Jiang Zhengnan

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
