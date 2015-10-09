package info.meizi;

import android.app.IntentService;
import android.content.Intent;

import com.squareup.okhttp.OkHttpClient;

import java.io.IOException;
import java.util.List;

import info.meizi.bean.MainBean;
import info.meizi.net.ContentParser;
import info.meizi.net.RequestFactory;
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

    @Override
    protected void onHandleIntent(Intent intent) {
        type = intent.getStringExtra("type");
        Intent resuleintent = new Intent(type);
        LogUtils.e("接受到命令");
        Realm realm = Realm.getInstance(this);

        List<MainBean> latest = MainBean.all(realm, type);

        if (!latest.isEmpty()) {//数据库有  直接发送广播通知

//           return;
        } else {//否则加载网络 并存入数据库 通知
            try {
                html = client.newCall(RequestFactory.make(type)).execute().body().string();
                LogUtils.e("获取成功");

                List<MainBean> list = ContentParser.ParserMainBean(html,type);
                saveDb(realm, list);
                LogUtils.e("存入数据库成功");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        sendBroadcast(resuleintent);
        LogUtils.e("发送广播");
        realm.close();
    }

    private void saveDb(Realm realm, List<MainBean> list) {
        realm.beginTransaction();
        realm.copyToRealm(list);
        realm.commitTransaction();
    }
}
