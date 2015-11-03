package info.meizi_retrofit.base;

import android.support.v7.app.AppCompatActivity;

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
        mSubscriptions = RxUtils.getNewCompositeSubIfUnsubscribed(mSubscriptions);
    }

    @Override
    public void onPause() {
        super.onPause();
        RxUtils.unsubscribeIfNotNull(mSubscriptions);
    }
}
