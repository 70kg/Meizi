package info.meizi_retrofit;

import android.app.Application;
import android.content.Context;

import com.avos.avoscloud.AVOSCloud;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Mr_Wrong on 15/10/30.
 */
public class App extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        AVOSCloud.initialize(this, BuildConfig.APPLICATIONID, BuildConfig.CLIENTKEY);
        RealmConfiguration myConfig = new RealmConfiguration.Builder(getContext())
                .name("Realm")
                .schemaVersion(2)
//                .setModules(new MyCustomSchema())
                .build();


        Realm.setDefaultConfiguration(myConfig);
    }

    public static Context getContext() {
        return mContext;
    }

}
