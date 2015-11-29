package info.meizi_retrofit.base;

import android.support.v7.app.AppCompatActivity;

import com.umeng.analytics.MobclickAgent;

import info.meizi_retrofit.utils.RxUtils;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Mr_Wrong on 15/10/31.
 */
public class BaseActivity extends AppCompatActivity {
    protected CompositeSubscription mSubscriptions = new CompositeSubscription();

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
}
