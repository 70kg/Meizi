package info.meizi_retrofit.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import com.socks.library.KLog;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Mr_Wrong on 15/11/6.
 */
public class RxMeizhi {
    public static Observable<Uri> saveImageAndGetPathObservable(final Context context, final String url, final String name) {
        return saveImagesAndGetPathObservable(context, url, "Meizi", name);
    }

    public static Observable<Uri> saveImagesAndGetPathObservable(final Context context, final String url, final String foldername, final String name) {
        return Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                Bitmap bitmap = null;
                try {
                    bitmap = Picasso.with(context).load(url).get();
                } catch (IOException e) {
                    subscriber.onError(e);
                }
                if (bitmap == null) {
                    subscriber.onError(new Exception("无法下载到图片"));
                }
                subscriber.onNext(bitmap);
                subscriber.onCompleted();
            }
        }).flatMap(new Func1<Bitmap, Observable<Uri>>() {
            @Override
            public Observable<Uri> call(Bitmap bitmap) {
                KLog.d(bitmap.getByteCount());
                return Observable.just(saveimages(context, bitmap, foldername, name));
            }
        }).subscribeOn(Schedulers.io());
    }

    public static Uri saveimages(Context context, Bitmap bm, String foldername, String name) {
        File appDir = new File(Environment.getExternalStorageDirectory(), foldername);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        File file = new File(appDir, name + ".jpg");
        KLog.e(file.getName());
        try {
            FileOutputStream out = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Uri uri = Uri.fromFile(file);
        // 通知图库更新
        Intent scannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
        context.sendBroadcast(scannerIntent);
        return uri;
    }


}