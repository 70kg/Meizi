这是个新开的坑，数据来源http://www.mzitu.com/

ps:如果要运行项目需要使用LeanCloud帐号问题，去Issues里面提

[apk下载地址](http://coolapk.com/apk/info.meizi_retrofit)

todo:

下一步打算把数据都抓到LeanCloud上，增加更多可交互性的功能，比如评价，打分等

---我是分割线---

使用的开源库：
* butterknife
* okhttp
* picasso
* realm
* jsoup 
* Palette实现toolbar和statebar根据图片变色

![](https://github.com/70kg/Meizi/blob/master/screenshots/meizi_1.png)
![](https://github.com/70kg/Meizi/blob/master/screenshots/meizi_2.png)
![](https://github.com/70kg/Meizi/blob/master/screenshots/meizi_3.png)
![](https://github.com/70kg/Meizi/blob/master/screenshots/meizi_4.png)

下一步开始尝试使用RxJava和Retrofit进行代码重构，同时增加保存，分享，定时更新唤醒等功能，还有要更适合Material Design
---
---
算是重构完了，感觉写的还可以吧，也不是特别满意，重写了界面，使用了RxJava和Retrofit进行请求，具体看meizi_retrofi的tmodel

![](https://github.com/70kg/Meizi/blob/master/screenshots/meizi_21.png)
![](https://github.com/70kg/Meizi/blob/master/screenshots/meizi_22.png)
![](https://github.com/70kg/Meizi/blob/master/screenshots/meizi_23.png)
![](https://github.com/70kg/Meizi/blob/master/screenshots/meizi_24.png)

参考了@XiNGRZ 和@drakeet的项目，感谢
