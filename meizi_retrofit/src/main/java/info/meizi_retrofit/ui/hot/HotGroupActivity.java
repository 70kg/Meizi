package info.meizi_retrofit.ui.hot;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.socks.library.KLog;

import info.meizi_retrofit.R;
import info.meizi_retrofit.ui.base.ToolBarActivity;
import info.meizi_retrofit.ui.main.HomeFragment;

/**
 * Created by Mr_Wrong on 16/1/8.
 */
public class HotGroupActivity extends ToolBarActivity {
    public static final String TAG = "tag";
    public static final String TITLE = "title";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String tag = getIntent().getStringExtra(TAG);
        KLog.e(tag);
        replaceFragment(HomeFragment.newFragment(tag));
        setTitle(getIntent().getStringExtra(TITLE));
    }

    @Override
    protected int layoutId() {
        return R.layout.tag_group;
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.layout_content, fragment);
        transaction.commit();
    }
}
