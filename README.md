# NguiLib
English | [简体中文](./README.zh-CN.md) |
</br>
![Java](https://img.shields.io/badge/language-Kotlin-red.svg)
![Kotlin](https://img.shields.io/badge/language-Java-blue.svg)
![visitors](https://visitor-badge.laobi.icu/badge?page_id=jiangzhengnan.nguilib.read.me)
</br>
An *beautiful+useful+easy-style*  custom view & layout collection for Android,minSdkVersion >= 14<br>
issues welcome<br/>
Jcenter: 
<a href="https://bintray.com/jiangzhengnan/NguiLib/NguiLib">https://bintray.com/jiangzhengnan/NguiLib/NguiLib</a><br />

## UseAge
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
To add gradle dependency you need to open build.gradle (in your app folder,not in a project folder) then copy and add the dependencies there in the dependencies block
<br/>

## Layout 
1.ZoomLayout. Linelayout supporting linkage drag and size change.<br />
<img src="https://github.com/jiangzhengnan/NguiLib/blob/master/app/src/main/res/raw/zoom_layout.gif" />

## View 
1.CentralTractionButton. Perfect Realization of Tab Button Animation Effect of Tencent QQ   <br />
Information: <a href="https://blog.csdn.net/qq_22770457/article/details/78630695">腾讯QQ的Tab按钮动画效果完美实现</a><br />
<img src="https://github.com/jiangzhengnan/NguiLib/blob/master/app/src/main/res/raw/ctb_show.gif" /><br /><br />
2.EcgView. ECG display <br />
Information: <a href="https://blog.csdn.net/qq_22770457/article/details/90679481">ECG心电图的绘制实现</a><br />
<img src="https://github.com/jiangzhengnan/NguiLib/blob/master/app/src/main/res/raw/ecg_show.gif" /><br /><br />
3.PolygonLoadView. A Cool Load View<br />
Inspiration : <a href="https://dribbble.com/shots/5878367-Loaders">https://dribbble.com/shots/5878367-Loaders</a><br />
<img src="https://github.com/jiangzhengnan/NguiLib/blob/master/app/src/main/res/raw/pl_show.gif" /> <br /><br />
4.ArrowInteractionView. A Load Animation Button<br />
Inspiration : <a href="https://dribbble.com/shots/6201452-Arrow-micro-interaction">https://dribbble.com/shots/6201452-Arrow-micro-interaction</a><br />
<img src="https://github.com/jiangzhengnan/NguiLib/blob/master/app/src/main/res/raw/ai_show.gif" /> <br /><br />
5.PointLoadingView. A Point Load Animation View<br />
<img src="https://github.com/jiangzhengnan/NguiLib/blob/master/app/src/main/res/raw/ptl_show.gif" /> <br /><br />
6.CylinderView. A 3D cylinder animation View<br />
Inspiration : <a href="https://dribbble.com/shots/7077455-Spending-analytics">https://dribbble.com/shots/7077455-Spending-analytics</a><br />
<img src="https://github.com/jiangzhengnan/NguiLib/blob/master/app/src/main/res/raw/cd_show.gif" /> <br /><br />
7.ToggleView. A simple vector switching animation<br />
Inspiration : <a href="https://dribbble.com/shots/9834681--Toggle">https://dribbble.com/shots/9834681--Toggle</a><br />
<img src="https://github.com/jiangzhengnan/NguiLib/blob/master/app/src/main/res/raw/tg_show.gif" /> <br /><br />
8.BoundlessSeekBar. ProgressBar without boundary drag, supporting inertial sliding<br />
<img src="https://github.com/jiangzhengnan/NguiLib/blob/master/app/src/main/res/raw/bd_show.gif" /> <br /><br />
 
## Features
- [x] https://dribbble.com/shots/6612066-Tab-Bar-active-animation
## License

    Copyright 2021, Jiang Zhengnan

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
