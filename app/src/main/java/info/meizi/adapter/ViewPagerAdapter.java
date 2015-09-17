package info.meizi.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr_Wrong on 15/9/17.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    String[] titles = {"首页","性感妹子","日本妹子","台湾妹子","清纯妹子","妹子自拍"};
    List<Fragment> fragments = new ArrayList<>();
    public ViewPagerAdapter(FragmentManager fm,List<Fragment> fragments) {
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
