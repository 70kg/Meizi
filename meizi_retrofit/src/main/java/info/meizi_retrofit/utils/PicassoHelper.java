package info.meizi_retrofit.utils;

import android.content.Context;
import android.content.pm.PackageInfo;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Mr_Wrong on 16/1/15.
 */
public class PicassoHelper {
    public static void buildPicasso(Context context) {
        Picasso.setSingletonInstance(new Picasso.Builder(context)
                .downloader(new OkHttp3Downloader(new OkHttpClient.Builder()
                        .addInterceptor(new Interceptor() {
                            @Override
                            public Response intercept(Chain chain) throws IOException {
                                Request newRequest = chain.request().newBuilder()
                                        .addHeader("Referer", "http://www.mzitu.com")
                                        .build();
                                return chain.proceed(newRequest);
                            }
                        })
                        .build()))
                .build());
    }

    public static Picasso getInstance(Context context) {
        return Picasso.with(context);
    }
}
