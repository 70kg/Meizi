package info.meizi.ui.group;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.squareup.leakcanary.RefWatcher;

import butterknife.Bind;
import butterknife.ButterKnife;
import info.meizi.R;
import info.meizi.base.BaseActivity;
import info.meizi.base.MyApp;
import info.meizi.utils.LogUtils;
import info.meizi.utils.SystemBarTintManager;

/**
 * Created by Mr_Wrong on 15/10/10.
 */
public class GroupActivity extends BaseActivity {
    @Bind(R.id.group_toolbar)
    Toolbar mToolbar;
    private String groupid;
    private GroupFragment fragment;
    public int index = -1;
    public int color;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supportFinishAfterTransition();
            }
        });
        groupid = getIntent().getStringExtra("groupid");
        color = getIntent().getIntExtra("color", getResources().getColor(R.color.app_primary_color));
        LogUtils.d("PaletteColor----->" + color);

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        mToolbar.setBackgroundColor(color);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            tintManager.setStatusBarTintColor(color);
        }
        setDefaultFragment();
    }

    //这里有问题。。还不知道怎么去解决
    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        supportStartPostponedEnterTransition();
        index = data.getIntExtra("index", 0);
    }


    public int getIndex() {
        return index;
    }

    private void setDefaultFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        if (fragment == null) {
            fragment = new GroupFragment(groupid);
        }
        transaction.replace(R.id.content, fragment);
        transaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = MyApp.getRefWatcher(this);
        refWatcher.watch(this);
    }
}
