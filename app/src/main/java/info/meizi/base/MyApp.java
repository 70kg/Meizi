package info.meizi.base;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by Mr_Wrong on 15/9/14.
 */
public class MyApp extends Application {
    private static Context mContext;
    private RefWatcher mRefWatcher;
    @Override
    public void onCreate() {
        super.onCreate();
        mRefWatcher = LeakCanary.install(this);
        mContext = getApplicationContext();
        initImageLoader();
    }
    public static Context getContext() {
        return mContext;
    }
    public static RefWatcher getRefWatcher(Context context) {
        MyApp application = (MyApp) context.getApplicationContext();
        return application.mRefWatcher;
    }

    // 初始化ImageLoader
    public static void initImageLoader() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        ImageLoader.getInstance().init(config);
    }
}
