package info.meizi.net;

import android.app.IntentService;
import android.content.Intent;

import com.squareup.okhttp.OkHttpClient;

import java.io.IOException;
import java.util.List;

import info.meizi.bean.MainBean;
import info.meizi.utils.LogUtils;
import info.meizi.utils.Utils;
import io.realm.Realm;

/**
 * Created by Mr_Wrong on 15/10/9.
 */
public class MainService extends IntentService {
    private static final String TAG = "MainService";
    private final OkHttpClient client = new OkHttpClient();

    public MainService() {
        super(TAG);
    }

    private String type;//首页的类型  性感，日本。。
    private String html;
    private String mPage;//加载更多的

    @Override
    protected void onHandleIntent(Intent intent) {
        type = intent.getStringExtra("type");
        mPage = intent.getStringExtra("page");
        //返回结果的
        Intent resuleintent = new Intent(type);
        Realm realm = Realm.getInstance(this);

        List<MainBean> latest = MainBean.all(realm, type);

        if (!latest.isEmpty() && latest.size() >= Page2int(mPage) * 24) {//数据库有  直接发送广播通知


        } else {//否则加载网络 并存入数据库 通知
            try {
                html = client.newCall(RequestFactory.make(Utils.makeUrl(type, mPage))).execute().body().string();
                LogUtils.d("http://www.mzitu.com/" + Utils.makeUrl(type, mPage));
                List<MainBean> list = ContentParser.ParserMainBean(html, type);
                saveDb(realm, list);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        sendBroadcast(resuleintent);
        LogUtils.d("发送广播" + type);
        realm.close();
    }

    private void saveDb(Realm realm, List<MainBean> list) {
        realm.beginTransaction();
        realm.copyToRealm(list);
        realm.commitTransaction();
        LogUtils.d("存入数据库成功");
    }
    int Page2int(String s) {
        int i = 1;
        if (!s.equals("")) {
            i = Integer.parseInt(s);
        }
        return i;
    }
}
