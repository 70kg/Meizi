package info.meizi_retrofit.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.avos.avoscloud.AVUser;
import com.umeng.analytics.MobclickAgent;

import info.meizi_retrofit.model.SaveDb;
import info.meizi_retrofit.utils.BusProvider;
import info.meizi_retrofit.utils.RxUtils;
import io.realm.Realm;
import io.realm.RealmObject;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Mr_Wrong on 15/10/31.
 */
public class BaseActivity<T extends RealmObject> extends AppCompatActivity implements SaveDb<T> {
    protected CompositeSubscription mSubscriptions = new CompositeSubscription();
    protected Realm realm;
    protected boolean mHasUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
        mHasUser = AVUser.getCurrentUser() != null;
        BusProvider.register(this);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
        BusProvider.unregister(this);
    }

    @Override
    public void saveDb(T t) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(t);
        realm.commitTransaction();
    }

    @Override
    public void saveDb(Iterable<T> objects) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(objects);
        realm.commitTransaction();
    }
}
