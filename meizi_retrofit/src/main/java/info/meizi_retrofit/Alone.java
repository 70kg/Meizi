package info.meizi_retrofit;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.socks.library.KLog;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.text.DecimalFormat;

/**
 * Created by Mr_Wrong on 15/12/6.
 */
public class Alone {
    public static void main(String[] args) {
        String url = "http://pic.mmfile.net/2016/01/06r20.jpg";
        int i = 1;
        System.out.println(String.format("%s%s%s", url.substring(0, 33), new DecimalFormat("00").format(i), ".jpg"));


        HandlerThread thread = new HandlerThread("okhttp");
        thread.start();
        Handler handler = new Handler(thread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url("http://70kg.info").build();
                try {
                    Response response = client.newCall(request).execute();
                    KLog.e(response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };


    }
}
