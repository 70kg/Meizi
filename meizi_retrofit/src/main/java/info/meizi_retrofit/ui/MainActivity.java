package info.meizi_retrofit.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.umeng.update.UmengUpdateAgent;

import butterknife.Bind;
import butterknife.ButterKnife;
import info.meizi_retrofit.R;
import info.meizi_retrofit.base.BaseActivity;
import info.meizi_retrofit.ui.fragment.HomeFragment;
import info.meizi_retrofit.utils.Utils;
import me.drakeet.materialdialog.MaterialDialog;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.id_nv_menu)
    NavigationView mMenu;
    @Bind(R.id.navigation_drawer_bottom)
    NavigationView mBottomMenu;
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
        mBottomMenu.setNavigationItemSelectedListener(this);
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
                startActivity(new Intent(this,CollectedActivity.class));
                break;
        }
        mDrawerLayout.closeDrawers();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.input_groupid://显示输入框
                final EditText content = new EditText(this);
                content.setHint("输入ID，具体可以查看网站URL里面的数字，类似50087");
                final MaterialDialog materialDialog = new MaterialDialog(this);
                materialDialog.setTitle("输入ID");
                materialDialog.setView(content);
                materialDialog.setPositiveButton("查看", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        materialDialog.dismiss();
                        String groupid = content.getText().toString().trim();
                        if (!TextUtils.isEmpty(groupid)) {
                            Toast.makeText(MainActivity.this, "ID可能是合法的", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, GroupActivity.class);
                            intent.putExtra(GroupActivity.GROUPID, groupid);
                            startActivity(intent);
                        } else {
                            Toast.makeText(MainActivity.this, "ID不合法", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                materialDialog.setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        materialDialog.dismiss();
                    }
                });
                materialDialog.show();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, fragment);
        transaction.commit();
    }

}
