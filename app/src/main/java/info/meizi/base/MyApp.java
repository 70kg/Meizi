package info.meizi.base;

import android.app.Application;
import android.content.Context;

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
    }
    public static Context getContext() {
        return mContext;
    }
    public static RefWatcher getRefWatcher(Context context) {
        MyApp application = (MyApp) context.getApplicationContext();
        return application.mRefWatcher;
    }
}
