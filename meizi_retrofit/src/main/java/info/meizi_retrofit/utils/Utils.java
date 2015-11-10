package info.meizi_retrofit.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;

/**
 * Created by Mr_Wrong on 15/10/13.
 */
public class Utils {
    public static String url2groupid(String url) {
        return url.split("/")[3];
    }

    public static String makeUrl(String type, String count) {
        String url;
        String page = "";
        if (type.equals("")) {
            page = "page/";
            if (count.equals("")) {
                page = "";
            }
        } else {
            page = "/page/";
            if (count.equals("")) {
                page = "";
            }
        }
        LogUtils.e("http://www.mzitu.com/" + type + page + count);
        return type + page + count;
    }

    public static int getPaletteColor(Bitmap bitmap) {
        int color = -12417291;
        Palette p = Palette.from(bitmap).generate();
        Palette.Swatch vibrant =
                p.getVibrantSwatch();
        Palette.Swatch vibrantdark =
                p.getDarkVibrantSwatch();
        Palette.Swatch vibrantlight =
                p.getLightVibrantSwatch();
        Palette.Swatch Muted =
                p.getMutedSwatch();
        Palette.Swatch Muteddark =
                p.getDarkMutedSwatch();
        Palette.Swatch Mutedlight =
                p.getLightMutedSwatch();

        if (vibrant != null) {
            color = vibrant.getRgb();
        } else if (vibrantdark != null) {
            color = vibrantdark.getRgb();
        } else if (vibrantlight != null) {
            color = vibrantlight.getRgb();
        } else if (Muted != null) {
            color = Muted.getRgb();
        } else if (Muteddark != null) {
            color = Muteddark.getRgb();
        } else if (Mutedlight != null) {
            color = Mutedlight.getRgb();
        }
        return color;
    }

    public static void statrtRefresh(final SwipeRefreshLayout mRefresher, final boolean isrefresh) {
        mRefresher.post(new Runnable() {
            @Override
            public void run() {
                mRefresher.setRefreshing(isrefresh);
            }
        });
    }

    public static  void setSystemBar(Activity context,Toolbar mToolbar,int color) {
        SystemBarTintManager tintManager = new SystemBarTintManager(context);
        tintManager.setStatusBarTintEnabled(true);
        mToolbar.setBackgroundColor(color);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            tintManager.setStatusBarTintColor(color);
        }
    }
}
