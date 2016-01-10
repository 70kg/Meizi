package info.meizi_retrofit.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr_Wrong on 16/1/10.
 */
public class HotPageAdapter extends FragmentPagerAdapter {
    String[] titles = {"热门主题","热门妹子"};
    List<Fragment> fragments = new ArrayList<>();
    public HotPageAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments= fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
