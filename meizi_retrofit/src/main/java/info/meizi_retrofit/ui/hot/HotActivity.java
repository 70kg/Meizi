package info.meizi_retrofit.ui.hot;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import info.meizi_retrofit.R;
import info.meizi_retrofit.adapter.HotPageAdapter;
import info.meizi_retrofit.ui.base.ToolBarActivity;
import info.meizi_retrofit.ui.hot.girl.HotGirlFragment;
import info.meizi_retrofit.ui.hot.tag.HotTagFragment;

/**
 * Created by Mr_Wrong on 16/1/10.
 */
public class HotActivity extends ToolBarActivity {
    @Bind(R.id.ac_tab_vp)
    ViewPager mViewPager;
    @Bind(R.id.tab_layout)
    TabLayout mTabLayout;
    List<Fragment> mFragments = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("热门");
        initViews();
    }

    private void initViews() {
        mTabLayout.setTabTextColors(Color.WHITE, Color.RED);
        mTabLayout.setSelectedTabIndicatorColor(Color.RED);
        mTabLayout.addTab(mTabLayout.newTab().setText("首页"));
        mTabLayout.addTab(mTabLayout.newTab().setText("性感妹子"));

        mFragments.add(HotTagFragment.newFragment());
        mFragments.add(HotGirlFragment.newFragment());

        mViewPager.setAdapter(new HotPageAdapter(getSupportFragmentManager(), mFragments));
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected int layoutId() {
        return R.layout.hot;
    }
}
