package info.meizi_retrofit.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.umeng.analytics.MobclickAgent;

import info.meizi_retrofit.model.Content;
import info.meizi_retrofit.utils.LogUtils;
import info.meizi_retrofit.utils.RxUtils;
import io.realm.Realm;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Mr_Wrong on 15/10/31.
 */
public class BaseActivity extends AppCompatActivity {
    protected CompositeSubscription mSubscriptions = new CompositeSubscription();
    protected Realm realm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        mSubscriptions = RxUtils.getNewCompositeSubIfUnsubscribed(mSubscriptions);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        RxUtils.unsubscribeIfNotNull(mSubscriptions);
    }

    protected void saveDB(Content content) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(content);
        realm.commitTransaction();
        LogUtils.d("存入数据库");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
