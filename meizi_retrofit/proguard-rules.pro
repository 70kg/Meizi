# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/Mr_Wrong/Downloads/adt-bundle-mac-x86_64-20140702/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
 #指定代码的压缩级别
    -optimizationpasses 5

    #包明不混合大小写
    -dontusemixedcaseclassnames

    #不去忽略非公共的库类
    -dontskipnonpubliclibraryclasses

     #优化  不优化输入的类文件
    -dontoptimize

     #预校验
    -dontpreverify

     #混淆时是否记录日志
    -verbose

     # 混淆时所采用的算法
    -optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

    #保护注解
    -keepattributes *Annotation*

    # 保持哪些类不被混淆
    -keep public class * extends android.app.Fragment
    -keep public class * extends android.app.Activity
    -keep public class * extends android.app.Application
    -keep public class * extends android.app.Service
    -keep public class * extends android.content.BroadcastReceiver
    -keep public class * extends android.content.ContentProvider
    -keep public class * extends android.app.backup.BackupAgentHelper
    -keep public class * extends android.preference.Preference
    -keep public class com.android.vending.licensing.ILicensingService
    #如果有引用v4包可以添加下面这行
    -keep public class * extends android.support.v4.app.Fragment

    -keepattributes InnerClasses
    -keep class io.realm.annotations.RealmModule
    -keep @io.realm.annotations.RealmModule class *
    -keep class io.realm.internal.Keep
    -keep @io.realm.internal.Keep class *
    -dontwarn javax.**
    -dontwarn io.realm.**

    #butterknife
   -keep class butterknife.** { *; }
   -dontwarn butterknife.internal.**
   -keep class **$$ViewBinder { *; }

   -keepclasseswithmembernames class * {
       @butterknife.* <fields>;
   }

   -keepclasseswithmembernames class * {
       @butterknife.* <methods>;
   }


    #忽略警告
    -ignorewarning

    ##记录生成的日志数据,gradle build时在本项目根目录输出##

    #apk 包内所有 class 的内部结构
    -dump class_files.txt
    #未混淆的类和成员
    -printseeds seeds.txt
    #列出从 apk 中删除的代码
    -printusage unused.txt
    #混淆前后的映射
    -printmapping mapping.txt

    #如果不想混淆 keep 掉
    -keep class com.lippi.recorder.iirfilterdesigner.** {*; }
    #忽略警告
    -dontwarn com.lippi.recorder.utils**
    #保留一个完整的包
    -keep class com.lippi.recorder.utils.** {
        *;
     }

    -keep class  com.lippi.recorder.utils.AudioRecorder{*;}


    ####混淆保护自己项目的部分代码以及引用的第三方jar包library-end####

    -keep public class * extends android.view.View {
        public <init>(android.content.Context);
        public <init>(android.content.Context, android.util.AttributeSet);
        public <init>(android.content.Context, android.util.AttributeSet, int);
        public void set*(...);
    }

    #保持 native 方法不被混淆
    -keepclasseswithmembernames class * {
        native <methods>;
    }

    #保持自定义控件类不被混淆
    -keepclasseswithmembers class * {
        public <init>(android.content.Context, android.util.AttributeSet);
    }

    #保持自定义控件类不被混淆
    -keepclassmembers class * extends android.app.Activity {
       public void *(android.view.View);
    }

    #保持 Parcelable 不被混淆
    -keep class * implements android.os.Parcelable {
      public static final android.os.Parcelable$Creator *;
    }

    #保持 Serializable 不被混淆
    -keepnames class * implements java.io.Serializable

    #保持 Serializable 不被混淆并且enum 类也不被混淆
    -keepclassmembers class * implements java.io.Serializable {
        static final long serialVersionUID;
        private static final java.io.ObjectStreamField[] serialPersistentFields;
        !static !transient <fields>;
        !private <fields>;
        !private <methods>;
        private void writeObject(java.io.ObjectOutputStream);
        private void readObject(java.io.ObjectInputStream);
        java.lang.Object writeReplace();
        java.lang.Object readResolve();
    }

    -keepclassmembers class * {
        public void *ButtonClicked(android.view.View);
    }

    #不混淆资源类
    -keepclassmembers class **.R$* {
        public static <fields>;
    }

    # OkHttp
    -keepattributes Signature
    -keep class com.squareup.okhttp.** { *; }
    -keep interface com.squareup.okhttp.** { *; }
    -dontwarn com.squareup.okhttp.**

    -keepclasseswithmembers class * {
        @retrofit.http.* <methods>;
    }

    # If in your rest service interface you use methods with Callback argument.
    -keepattributes Exceptions


    # For using GSON @Expose annotation
    -keepattributes EnclosingMethod
     # Gson specific classes
    -keep class sun.misc.Unsafe { *; }
    -keep class com.google.gson.stream.** { *; }

    # Retrofit 1.X
    -keep class retrofit.** { *; }
    -dontwarn okio.**
    -dontwarn retrofit.**
    -dontwarn rx.**

    -keepclasseswithmembers class * {
        @retrofit.http.* <methods>;
    }

    # RxJava 0.21

    -keep class rx.schedulers.Schedulers {
        public static <methods>;
    }
    -keep class rx.schedulers.ImmediateScheduler {
        public <methods>;
    }
    -keep class rx.schedulers.TestScheduler {
        public <methods>;
    }
    -keep class rx.schedulers.Schedulers {
        public static ** test();
    }

    -keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
        long producerIndex;
        long consumerIndex;
    }
    -keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
        rx.internal.util.atomic.LinkedQueueNode producerNode;
    }
    -keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
        rx.internal.util.atomic.LinkedQueueNode consumerNode;
    }


    -dontwarn com.jcraft.jzlib.**
    -keep class com.jcraft.jzlib.**  { *;}

    -dontwarn sun.misc.**
    -keep class sun.misc.** { *;}

    -dontwarn com.alibaba.fastjson.**
    -keep class com.alibaba.fastjson.** { *;}

    -dontwarn sun.security.**
    -keep class sun.security.** { *; }

    -dontwarn com.google.**
    -keep class com.google.** { *;}

    -dontwarn com.avos.**
    -keep class com.avos.** { *;}

    -keep public class android.net.http.SslError
    -keep public class android.webkit.WebViewClient

    -dontwarn android.webkit.WebView
    -dontwarn android.net.http.SslError
    -dontwarn android.webkit.WebViewClient

    -dontwarn android.support.**

    -dontwarn org.apache.**
    -keep class org.apache.** { *;}

    -dontwarn org.jivesoftware.smack.**
    -keep class org.jivesoftware.smack.** { *;}

    -dontwarn com.loopj.**
    -keep class com.loopj.** { *;}


    -dontwarn org.xbill.**
    -keep class org.xbill.** { *;}

    -dontwarn com.socks.library.**
    -keep class com.socks.library.** { *; }

    -keepclassmembers class ** {
        @com.squareup.otto.Subscribe public *;
        @com.squareup.otto.Produce public *;
    }

-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}
-keep class android.support.**{*;}