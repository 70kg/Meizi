package info.meizi.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import info.meizi.R;
import info.meizi.base.BaseActivity;
import info.meizi.bean.Content;
import io.realm.Realm;

/**
 * Created by Mr_Wrong on 15/10/6.
 */
public class LargePicActivity extends BaseActivity {
    @Bind(R.id.large_toolbar)
    Toolbar mToolbar;
    @Bind(R.id.pager)
    ViewPager mPager;
    private int index;
    private Realm realm;
    private List<Content> images;
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
        setSupportActionBar(mToolbar);

        index = getIntent().getIntExtra("index", 0);
        groupid = getIntent().getStringExtra("groupid");

        realm = Realm.getInstance(this);
        images = Content.all(realm, groupid);

        adapter = new PagerAdapter();

        mPager.setAdapter(adapter);
        mPager.setCurrentItem(index);


        setEnterSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                Content image = images.get(mPager.getCurrentItem());
                LargePicFragment fragment = (LargePicFragment) adapter.instantiateItem(mPager, mPager.getCurrentItem());

                sharedElements.clear();
                sharedElements.put(image.getUrl(), fragment.getSharedElement());
            }
        });
    }
    @Override
    public void supportFinishAfterTransition() {
        Intent data = new Intent();
        data.putExtra("index", mPager.getCurrentItem());
        setResult(RESULT_OK, data);
//        showSystemUi();
        super.supportFinishAfterTransition();
    }

    private class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter() {
            super(getSupportFragmentManager());
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public Fragment getItem(int position) {
            return LargePicFragment.newFragment(
                    images.get(position).getUrl(),
                    position == index);
        }

    }
}
