package info.meizi_retrofit.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import info.meizi_retrofit.R;
import info.meizi_retrofit.base.BaseActivity;
import info.meizi_retrofit.widget.PullScrollView;

/**
 * Created by Mr_Wrong on 15/11/29.
 */
public class AboutActivity extends BaseActivity {
//    private static final String TAG = "AboutActivity";logt
    //private static final int  = 637;//const
    //private static final String KEY_ = "";//key
//    public static AboutActivity newInstance() {
//
//        Bundle args = new Bundle();
//
//        AboutActivity fragment = new AboutActivity();
//        fragment.setArguments(args);
//        return fragment;
//    }newinstance

    ///////////////////////////////////////////////////////////////////////////
    // sbc
    ///////////////////////////////////////////////////////////////////////////

//    String.format("", ); sfmt

//    public static void start(Context context) {
//        Intent starter = new Intent(context, AboutActivity.class);
//        starter.putExtra();
//        context.startActivity(starter);
//    }starter

//    Toast.makeText(AboutActivity.this, "", Toast.LENGTH_SHORT).show();  toast

//cons  自定义view的时候构造函数


    @Bind(R.id.tb_about)
    Toolbar mToolbar;
    @Bind(R.id.background_img)
    ImageView backgroundImg;
    @Bind(R.id.scroll_view)
    PullScrollView scrollView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        ButterKnife.bind(this);
        scrollView.setHeader(backgroundImg);

//        Log.d(TAG, "onCreate: "); logd

        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
