package info.meizi_retrofit.utils;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Mr_Wrong on 15/10/30.
 */
public class RxUtils {
    public static void unsubscribeIfNotNull(Subscription subscription) {
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

    public static CompositeSubscription getNewCompositeSubIfUnsubscribed(CompositeSubscription subscription) {
        if (subscription == null || subscription.isUnsubscribed()) {
            return new CompositeSubscription();
        }

        return subscription;
    }
}
