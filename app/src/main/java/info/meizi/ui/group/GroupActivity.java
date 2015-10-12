package info.meizi.ui.group;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;

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
    String groupid;
    public Bundle reenterState;
    GroupFragment fragment;
    public int index = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supportFinishAfterTransition();
            }
        });


        groupid = getIntent().getStringExtra("groupid");
        setDefaultFragment();

    }


    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        supportStartPostponedEnterTransition();
        index = data.getIntExtra("index", 0);
    }

    public int getIndex() {
     return  index;
    }

    private void setDefaultFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        fragment = new GroupFragment(groupid);
        transaction.replace(R.id.content, fragment);
        transaction.commit();
    }


}
