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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.umeng.update.UmengUpdateAgent;

import butterknife.Bind;
import butterknife.ButterKnife;
import info.meizi_retrofit.R;
import info.meizi_retrofit.base.BaseActivity;
import info.meizi_retrofit.ui.CollectedActivity;
import info.meizi_retrofit.ui.LoginActivity;
import info.meizi_retrofit.ui.about.AboutActivity;
import info.meizi_retrofit.ui.hot.HotActivity;
import info.meizi_retrofit.utils.Utils;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.id_nv_menu)
    NavigationView mMenu;
    @Bind(R.id.layout_drawerlayouy)
    DrawerLayout mDrawerLayout;
    TextView mName;
    ImageView mHead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        UmengUpdateAgent.setDeltaUpdate(false);
        UmengUpdateAgent.update(this);
        initToolBar();
        replaceFragment(HomeFragment.newFragment(""));
        initDrawer();
        initUser();
    }

    private void initUser() {
        if (AVUser.getCurrentUser() != null) {//已经登录
            //设置姓名,头像
            mName.setText(AVUser.getCurrentUser().getMobilePhoneNumber());
        } else {
            mName.setText("点击头像登录");
        }
    }


    private void initDrawer() {
        mDrawerLayout.setDrawerListener(new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close));
        mMenu.setNavigationItemSelectedListener(this);
        View haedLayout = mMenu.getHeaderView(0);
        mName = (TextView) haedLayout.findViewById(R.id.tv_name);
        mHead = (ImageView) haedLayout.findViewById(R.id.iv_head);
        if (!mHasUser)//没有登录的时候才去登录
            mHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityForResult(new Intent(MainActivity.this, LoginActivity.class), 66);
                }
            });
    }

    private void initToolBar() {
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        Utils.setSystemBar(this, mToolbar, getResources().getColor(R.color.app_primary_color));
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
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
                startActivity(new Intent(this, CollectedActivity.class));
                break;
            case R.id.menu_tag:
                startActivity(new Intent(this, HotActivity.class));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 666) {
            initUser();
        }
    }
}
