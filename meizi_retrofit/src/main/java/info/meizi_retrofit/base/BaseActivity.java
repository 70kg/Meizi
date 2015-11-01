package info.meizi_retrofit.base;

import android.support.v7.app.AppCompatActivity;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by Mr_Wrong on 15/10/31.
 */
public class BaseActivity extends AppCompatActivity {
    protected CompositeSubscription mSubscriptions = new CompositeSubscription();//这个是持有订阅  用于生命周期

}
