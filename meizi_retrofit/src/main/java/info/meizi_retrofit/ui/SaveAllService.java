package info.meizi_retrofit.ui;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

import info.meizi_retrofit.R;
import info.meizi_retrofit.utils.RxMeizhi;

/**
 * Created by Mr_Wrong on 16/4/2.
 */
public class SaveAllService extends IntentService {
    static final String TAG = "SaveAllService";
    public static final String URLS = "urls";
    public static final String GROUPID = "groupid";
    public static final String TITLE = "title";
    public static final String RESOURCEI_D = "resourceId";
    private String groupId;
    private ArrayList<String> urls = new ArrayList<>();
    private String title;
    private int resourceId;

    public SaveAllService() {
        super(TAG);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent.hasExtra(GROUPID)) {
            groupId = intent.getStringExtra(GROUPID);
        }
        if (intent.hasExtra(TITLE)) {
            title = intent.getStringExtra(TITLE);
        }
        if (intent.hasExtra(URLS)) {
            urls = (ArrayList<String>) intent.getSerializableExtra(URLS);
        }

        if (intent.hasExtra(RESOURCEI_D)) {
            resourceId = intent.getIntExtra(RESOURCEI_D, 0);
        }
        NotificationManager mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("Picture Download")
                .setContentText("Download in progress")
                .setSmallIcon(R.drawable.ic_github);

        if (urls.size() > 0) {
            for (int i = 0; i < urls.size(); i++) {
                String url = urls.get(i);
                try {
                    Bitmap bitmap = Picasso.with(this).load(url).get();
                    RxMeizhi.saveimages(this, bitmap, "Meizi/" + title, groupId + "_" + i);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mBuilder.setProgress(urls.size(), i + 1, false);
                mNotifyManager.notify(1, mBuilder.build());
            }
        }
        if (resourceId != 0) {
            Bitmap bitmap;
            try {
                bitmap = Picasso.with(this).load(resourceId).get();
                RxMeizhi.saveimages(this, bitmap, "Meizi/" + title, groupId);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Toast.makeText(this, String.format("保存套图%s成功!", title), Toast.LENGTH_SHORT).show();
    }
}
