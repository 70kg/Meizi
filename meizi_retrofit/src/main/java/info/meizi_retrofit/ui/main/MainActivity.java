package info.meizi_retrofit.ui.main;

import android.app.SharedElementCallback;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
import com.socks.library.KLog;

import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import info.meizi_retrofit.R;
import info.meizi_retrofit.model.UpLoad;
import info.meizi_retrofit.ui.CollectedActivity;
import info.meizi_retrofit.ui.LoginActivity;
import info.meizi_retrofit.ui.about.AboutActivity;
import info.meizi_retrofit.ui.base.BaseActivity;
import info.meizi_retrofit.ui.hot.HotActivity;
import info.meizi_retrofit.ui.selfie.SelfieFragment;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.id_nv_menu)
    NavigationView mMenu;
    @Bind(R.id.layout_drawerlayouy)
    DrawerLayout mDrawerLayout;
    private TextView mName;
    private ImageView mHead;
    private SelfieFragment fragment;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initToolBar();
        replaceFragment(HomeFragment.newFragment(""));
        initDrawer();
        initUser();
        if (Build.VERSION.SDK_INT >= 22) {
            setExitSharedElementCallback(new SharedElementCallback() {
                @Override
                public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                    if (bundle != null) {
                        int i = bundle.getInt("index", 0);
                        sharedElements.clear();
                        names.clear();
                        sharedElements.put(fragment.mAdapter.get(i).getUrl(), fragment.mLayoutManager.findViewByPosition(i));
                        bundle = null;
                    }
                }
            });
        }
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
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        mMenu.setNavigationItemSelectedListener(this);
        View haedLayout = mMenu.getHeaderView(0);
        mName = (TextView) haedLayout.findViewById(R.id.tv_name);
        mHead = (ImageView) haedLayout.findViewById(R.id.iv_head);
        //没有登录的时候才去登录
        mHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AVUser.getCurrentUser() == null) {
                    startActivityForResult(new Intent(MainActivity.this, LoginActivity.class), 66);
                }
            }
        });
    }

    private void initToolBar() {
        setSupportActionBar(mToolbar);
//        Utils.setSystemBar(this, mToolbar, getResources().getColor(R.color.app_primary_color));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
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
            case R.id.menu_selfie:
                mToolbar.setTitle("妹子自拍");
                fragment = SelfieFragment.newFragment();
                replaceFragment(fragment);
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


    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        bundle = new Bundle(data.getExtras());
        if (fragment != null && fragment.isAdded()) {
            supportPostponeEnterTransition();
            fragment.scrollToPosition(bundle.getInt("index", 0));
        }
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, fragment);
        transaction.commitAllowingStateLoss();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_about) {
            startActivity(new Intent(MainActivity.this, AboutActivity.class));
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 666) {
            initUser();
        }
    }
}
