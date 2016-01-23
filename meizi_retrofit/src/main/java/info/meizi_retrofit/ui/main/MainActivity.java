package info.meizi_retrofit.ui.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.sns.SNS;
import com.avos.sns.SNSBase;
import com.avos.sns.SNSCallback;
import com.avos.sns.SNSException;
import com.avos.sns.SNSType;
import com.socks.library.KLog;
import com.umeng.update.UmengUpdateAgent;

import butterknife.Bind;
import butterknife.ButterKnife;
import info.meizi_retrofit.R;
import info.meizi_retrofit.base.BaseActivity;
import info.meizi_retrofit.ui.CollectedActivity;
import info.meizi_retrofit.ui.about.AboutActivity;
import info.meizi_retrofit.utils.Utils;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.id_nv_menu)
    NavigationView mMenu;
    @Bind(R.id.layout_drawerlayouy)
    DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        UmengUpdateAgent.setDeltaUpdate(false);
        UmengUpdateAgent.update(this);

        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        Utils.setSystemBar(this, mToolbar, getResources().getColor(R.color.app_primary_color));
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        mDrawerLayout.setDrawerListener(new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close));

        replaceFragment(HomeFragment.newFragment(""));
        mMenu.setNavigationItemSelectedListener(this);

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_home:
                mToolbar.setTitle("Meizi");
                replaceFragment(HomeFragment.newFragment(""));
                break;
            case R.id.menu_xinggan:
                mToolbar.setTitle("性感妹子");
                replaceFragment(HomeFragment.newFragment("xinggan"));
                break;
            case R.id.menu_taiwan:
                mToolbar.setTitle("台湾妹子");
                replaceFragment(HomeFragment.newFragment("taiwan"));
                break;
            case R.id.menu_riben:
                mToolbar.setTitle("日本妹子");
                replaceFragment(HomeFragment.newFragment("japan"));
                break;
            case R.id.menu_qingchun:
                mToolbar.setTitle("清纯妹子");
                replaceFragment(HomeFragment.newFragment("mm"));
                break;
            case R.id.menu_about:
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
                break;
            case R.id.menu_collect1:
//                testLogin();
                startActivity(new Intent(this, CollectedActivity.class));
                break;
            case R.id.menu_tag:
                testLoginWithWeb();
//                startActivity(new Intent(this, HotActivity.class));
                break;
        }
        mDrawerLayout.closeDrawers();
        return true;
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, fragment);
        transaction.commit();
    }

    SNSType type;

    //sso登录
    private void testLogin() {
        try {
            type = SNSType.AVOSCloudSNSSinaWeibo;
            SNS.setupPlatform(this, SNSType.AVOSCloudSNSSinaWeibo, "3216821683",
                    "3216821683",
                    "https://api.weibo.com/oauth2/default.html");

            SNS.loginWithCallback(MainActivity.this, SNSType.AVOSCloudSNSSinaWeibo,
                    new SNSCallback() {

                        @Override
                        public void done(SNSBase base, SNSException error) {

                            KLog.e(base.authorizedData());
                            if (error == null) {
                                SNS.loginWithAuthData(base.userInfo(), new LogInCallback<AVUser>() {
                                    @Override
                                    public void done(AVUser user, AVException error) {
                                        KLog.e(error);
                                    }
                                });
                            }
                        }
                    });
        } catch (AVException e) {
            e.printStackTrace();
        }

    }

    //web登录
    private void testLoginWithWeb() {
        try {
            type = SNSType.AVOSCloudSNSSinaWeibo;
            SNS.setupPlatform(SNSType.AVOSCloudSNSSinaWeibo,
                    "https://leancloud.cn/1.1/sns/goto/eys0ndm7dp1tre7v");
            SNS.loginWithCallback(MainActivity.this, SNSType.AVOSCloudSNSSinaWeibo,
                    new SNSCallback() {
                        @Override
                        public void done(SNSBase base, SNSException error) {

                            KLog.e(base.authorizedData());
                            KLog.e(error);

                            if (error == null) {
                                SNS.loginWithAuthData(base.userInfo(), new LogInCallback<AVUser>() {
                                    @Override
                                    public void done(AVUser user, AVException error) {

                                    }
                                });
                            }
                        }
                    });
        } catch (AVException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SNS.onActivityResult(requestCode, resultCode, data, type);
    }
}
