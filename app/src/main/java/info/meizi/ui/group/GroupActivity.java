package info.meizi.ui.group;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import info.meizi.R;
import info.meizi.base.BaseActivity;

/**
 * Created by Mr_Wrong on 15/10/10.
 */
public class GroupActivity extends BaseActivity {
    @Bind(R.id.group_toolbar)
    Toolbar mToolbar;
    @Bind(R.id.content)
    FrameLayout content;
    String groupid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        groupid = getIntent().getStringExtra("groupid");
        setDefaultFragment();

    }

    private void setDefaultFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        GroupFragment fragment = new GroupFragment(groupid);
        transaction.replace(R.id.content, fragment);
        transaction.commit();
    }
}
