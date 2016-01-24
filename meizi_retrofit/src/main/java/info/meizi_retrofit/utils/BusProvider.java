package info.meizi_retrofit.utils;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;

/**
 * Created by Mr_Wrong on 16/1/12.
 */
public class BusProvider {
    private volatile static Bus bus = null;

    private static Bus getBus() {
        if (bus == null) {
            synchronized (Bus.class) {
                if (bus == null) {
                    bus = new Bus();
                }
            }
        }
        return bus;
    }

    public static void post(final Object o) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            getBus().post(o);
        } else {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    getBus().post(o);
                }
            });
        }

    }

    public static void register(Object o) {
        getBus().register(o);
    }

    public static void unregister(Object o) {
        getBus().unregister(o);
    }
}
