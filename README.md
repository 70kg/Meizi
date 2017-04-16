## ToDo

下一步打算把数据都抓到LeanCloud上，增加更多可交互性的功能，比如评价，打分等，也可以提高性能。等我先去入Python这个坑😂

## Update
如果要运行项目需要使用LeanCloud帐号问题，去Issues里面提

[apk下载地址](http://coolapk.com/apk/info.meizi_retrofit)

---

这是个新开的坑，数据来源http://www.mzitu.com/

刚开始是使用了okhttp加intentService进行的异步网络请求，具体参考app这个model，后来又使用了rxJava和retrofit进行了网络层的重构，同时增加了LeanCloud的云端同步收藏等功能，还对界面进行了修改。详细参考meizi_retrofi这个model。


使用的开源库：

* butterknife
* okhttp
* picasso
* realm
* jsoup 
* Palette
* 参考了@XiNGRZ 和@drakeet的项目，感谢
* and so on..

![](https://github.com/70kg/Meizi/blob/master/screenshots/meizi_1.png)
![](https://github.com/70kg/Meizi/blob/master/screenshots/meizi_2.png)
![](https://github.com/70kg/Meizi/blob/master/screenshots/meizi_3.png)
![](https://github.com/70kg/Meizi/blob/master/screenshots/meizi_4.png)