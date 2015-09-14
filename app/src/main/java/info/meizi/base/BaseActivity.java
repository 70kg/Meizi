package info.meizi.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.ImageLoader;

import info.meizi.utils.LogUtils;

/**
 * Created by Mr_Wrong on 15/9/14.
 */
public class BaseActivity extends AppCompatActivity {
    protected Context context;
    protected ImageLoader imageLoader;
    public static RequestQueue mRequestQueue = Volley.newRequestQueue(MyApp.getContext());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageLoader = ImageLoader.getInstance();
    }
    protected void executeRequest(Request<?> request) {
        if (this != null) {
            request.setTag(this);
        }
        mRequestQueue.add(request);
        LogUtils.d("addRequest = " + request.getUrl());

    }
}
