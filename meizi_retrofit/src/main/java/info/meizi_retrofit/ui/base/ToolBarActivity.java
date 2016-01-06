package info.meizi_retrofit.ui.base;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.ButterKnife;
import info.meizi_retrofit.R;
import info.meizi_retrofit.base.BaseActivity;

/**
 * Created by Mr_Wrong on 16/1/6.
 */
public abstract class ToolBarActivity extends BaseActivity {
    protected Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutId());
        ButterKnife.bind(this);
        mToolbar = (Toolbar) findViewById(R.id.inc_toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setTitle(setTitle());
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    protected String setTitle() {
        return "";
    }

    protected abstract int layoutId();
}
