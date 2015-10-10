package info.meizi.ui.main;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import info.meizi.R;
import info.meizi.adapter.ViewPagerAdapter;
import info.meizi.base.BaseActivity;
import info.meizi.ui.group.GroupFragment;

public class MainActivity extends BaseActivity {
    private static final String URL = "http://www.mzitu.com/";
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.ac_tab_vp)
    ViewPager viewPager;
    private String GROUP_ID = "35666";

    List<Fragment> mFragments = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initviews();
    }

    private void initviews() {
        setSupportActionBar(toolbar);

        tabLayout.addTab(tabLayout.newTab().setText("首页"));
        tabLayout.addTab(tabLayout.newTab().setText("性感妹子"));
        tabLayout.addTab(tabLayout.newTab().setText("日本妹子"));
        tabLayout.addTab(tabLayout.newTab().setText("台湾妹子"));
        tabLayout.addTab(tabLayout.newTab().setText("清纯妹子"));
        tabLayout.addTab(tabLayout.newTab().setText("妹子自拍"));
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setTabTextColors(Color.GRAY, getResources().getColor(R.color.app_primary_color));

          //mFragments.add(new MainFragment("xinggan"));
        mFragments.add(new GroupFragment("49727"));
        mFragments.add(new GroupFragment("48990"));
        mFragments.add(new GroupFragment("48645"));
        mFragments.add(new GroupFragment("48915"));
        mFragments.add(new GroupFragment("48827"));

        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), mFragments));
        viewPager.setOffscreenPageLimit(5);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
