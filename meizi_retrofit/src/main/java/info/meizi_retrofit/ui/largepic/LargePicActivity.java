package info.meizi_retrofit.ui.largepic;

import android.annotation.TargetApi;
import android.app.SharedElementCallback;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import info.meizi_retrofit.R;
import info.meizi_retrofit.ui.base.BaseActivity;

/**
 * Created by Mr_Wrong on 15/10/6.
 * 查看大图的
 */
public class LargePicActivity extends BaseActivity {
    public static final String INDEX = "index";
    public static final String GROUPID = "groupid";
    public static final String URLS = "urls";
    @Bind(R.id.large_toolbar)
    Toolbar mToolbar;
    @Bind(R.id.pager)
    ViewPager mPager;
    private int index;
    protected ArrayList<String> urls;
    private PagerAdapter adapter;
    private String groupid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.large_pic);

        supportPostponeEnterTransition();//延缓执行 然后在fragment里面的控件加载完成后start

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

        index = getIntent().getIntExtra(INDEX, 0);
        groupid = getIntent().getStringExtra(GROUPID);

        urls = (ArrayList<String>) getIntent().getSerializableExtra(URLS);

        adapter = new PagerAdapter();

        mPager.setAdapter(adapter);
        mPager.setCurrentItem(index);
        if (Build.VERSION.SDK_INT >= 22) {
            //这个可以看做个管道  每次进入和退出的时候都会进行调用  进入的时候获取到前面传来的共享元素的信息
            //退出的时候 把这些信息传递给前面的activity
            //同时向sharedElements里面put view,跟对view添加transitionname作用一样
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
