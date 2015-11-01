package info.meizi_retrofit.ui;

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
import android.view.Menu;
import android.view.MenuItem;

import butterknife.Bind;
import butterknife.ButterKnife;
import info.meizi_retrofit.R;
import info.meizi_retrofit.base.BaseActivity;
import info.meizi_retrofit.ui.fragment.HomeFragment;

public class MainActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.id_nv_menu)
    NavigationView mMenu;
    @Bind(R.id.layout_drawerlayouy)
    DrawerLayout mDrawerLayout;

    private HomeFragment[] mHomeFragments = new HomeFragment[]{
            new HomeFragment(""),
            new HomeFragment("xinggan"),
            new HomeFragment("japan"),
            new HomeFragment("mm")};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }


        mDrawerLayout.setDrawerListener(new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close));

        replaceFragment(mHomeFragments[0]);
        mMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.menu_home:
                        mToolbar.setTitle("Meizi");
                        replaceFragment(mHomeFragments[0]);
                        break;
                    case R.id.menu_xinggan:
                        mToolbar.setTitle("性感妹子");
                        replaceFragment(mHomeFragments[1]);
                        break;
                    case R.id.menu_riben:
                        mToolbar.setTitle("日本妹子");
                        replaceFragment(mHomeFragments[2]);
                        break;
                    case R.id.menu_qingchun:
                        mToolbar.setTitle("清纯妹子");
                        replaceFragment(mHomeFragments[3]);
                        break;

                }
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
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
            case R.id.action_settings:
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
