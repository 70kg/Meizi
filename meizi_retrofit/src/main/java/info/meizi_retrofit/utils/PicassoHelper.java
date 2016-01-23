package info.meizi_retrofit.utils;

import android.content.Context;

import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

/**
 * Created by Mr_Wrong on 16/1/15.
 */
public class PicassoHelper {
    static volatile Picasso singleton = null;
    private static final int MAX_DISK_CACHE_SIZE = 512 * 1024 * 1024;

    public static Picasso getInstance(Context context) {
        if (singleton == null) {
            synchronized (Picasso.class) {
                if (singleton == null) {
                    singleton = new Picasso.Builder(context)
                            .downloader(new OkHttpDownloader(context, MAX_DISK_CACHE_SIZE))
                            .build();
                }
            }
        }
        return singleton;
    }
}
