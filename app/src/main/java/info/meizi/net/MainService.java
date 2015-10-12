package info.meizi.net;

import android.app.IntentService;
import android.content.Intent;

import com.squareup.okhttp.OkHttpClient;

import java.io.IOException;
import java.util.List;

import info.meizi.bean.MainBean;
import info.meizi.utils.LogUtils;
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

    String type;
    String html;
    String mCount;

    @Override
    protected void onHandleIntent(Intent intent) {
        type = intent.getStringExtra("type");
        mCount = intent.getStringExtra("count");
        Intent resuleintent = new Intent(type);
        Realm realm = Realm.getInstance(this);

        List<MainBean> latest = MainBean.all(realm, type);

        if (!latest.isEmpty() && latest.size() >= string2int(mCount) * 24) {//数据库有  直接发送广播通知

//           return;
        } else {//否则加载网络 并存入数据库 通知
            try {
                html = client.newCall(RequestFactory.make(makeurl(type, mCount))).execute().body().string();
                LogUtils.d("获取成功");
                LogUtils.d("http://www.mzitu.com/" + makeurl(type, mCount));

                List<MainBean> list = ContentParser.ParserMainBean(html, type);
                saveDb(realm, list);
                LogUtils.d("存入数据库成功");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        sendBroadcast(resuleintent);
        LogUtils.d("发送广播" + type);
        realm.close();
    }

    String makeurl(String type, String count) {
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

        return type + page + count;
    }

    int string2int(String s) {
        int i = 1;
        if (!s.equals("")) {
            i = Integer.parseInt(s);
        }
        return i;
    }

    private void saveDb(Realm realm, List<MainBean> list) {
        realm.beginTransaction();
        realm.copyToRealm(list);
        realm.commitTransaction();
    }
}
