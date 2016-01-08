package info.meizi_retrofit.ui.largepic;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import info.meizi_retrofit.R;
import info.meizi_retrofit.base.BaseActivity;
import info.meizi_retrofit.ui.group.GroupActivity;

/**
 * Created by Mr_Wrong on 15/10/6.
 * 查看大图的
 */
public class LargePicActivity extends BaseActivity {

    @Bind(R.id.large_toolbar)
    Toolbar mToolbar;
    @Bind(R.id.pager)
    ViewPager mPager;
    private int index;
    //    private Realm realm;
    //    private ArrayList<Content> images;
    protected ArrayList<String> urls;
    private PagerAdapter adapter;
    private String groupid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.large_pic);
        ButterKnife.bind(this);
        initviews();
    }

    private void initviews() {
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setTitle("Meizi");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supportFinishAfterTransition();
            }
        });

        index = getIntent().getIntExtra(GroupActivity.INDEX, 0);
        groupid = getIntent().getStringExtra(GroupActivity.GROUPID);

//        realm = Realm.getDefaultInstance();
//        images = Content.all(realm, groupid);
        urls = (ArrayList<String>) getIntent().getSerializableExtra(GroupActivity.URLS);

        adapter = new PagerAdapter();

        mPager.setAdapter(adapter);
        mPager.setCurrentItem(index);
        if (Build.VERSION.SDK_INT >= 22) {
            setEnterSharedElementCallback(new SharedElementCallback() {
                @Override
                public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                    String url = urls.get(mPager.getCurrentItem());
                    LargePicFragment fragment = (LargePicFragment) adapter.instantiateItem(mPager, mPager.getCurrentItem());
                    sharedElements.clear();
                    sharedElements.put(url, fragment.getSharedElement());
                }
            });
        }

    }

    @TargetApi(22)
    @Override
    public void supportFinishAfterTransition() {
        Intent data = new Intent();
        data.putExtra("index", mPager.getCurrentItem());
        setResult(RESULT_OK, data);
        super.supportFinishAfterTransition();
    }


    private class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter() {
            super(getSupportFragmentManager());
        }

        @Override
        public int getCount() {
            return urls.size();
        }

        @Override
        public Fragment getItem(int position) {
            return LargePicFragment.newFragment(
                    urls.get(position), groupid, position);
        }

    }
}
