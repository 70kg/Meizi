package info.meizi_retrofit.ui.base;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import butterknife.ButterKnife;
import info.meizi_retrofit.R;
import info.meizi_retrofit.utils.Utils;

/**
 * Created by Mr_Wrong on 16/1/6.
 */
public abstract class ToolBarActivity extends BaseActivity {
    public static final String COLOR = "color";
    protected Toolbar mToolbar;
    protected int mColor;
    protected TextSwitcher mTextSwitcher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutId());
        ButterKnife.bind(this);
        mToolbar = (Toolbar) findViewById(R.id.inc_toolbar);
        mTextSwitcher = (TextSwitcher) findViewById(R.id.tv_title);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 22) {
                    supportFinishAfterTransition();
                } else {
                    finish();
                }

            }
        });
        mColor = getIntent().getIntExtra(COLOR, getResources().getColor(R.color.app_primary_color));
        Utils.setSystemBar(this, mToolbar, mColor);
        initTitle();
    }

    private void initTitle() {
        mTextSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                final TextView textView = new TextView(ToolBarActivity.this);
                textView.setTextAppearance(ToolBarActivity.this, R.style.WebTitle);
                textView.setSingleLine(true);
                textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                textView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        textView.setSelected(true);
                    }
                }, 1738);
                return textView;
            }
        });
        mTextSwitcher.setInAnimation(this, android.R.anim.fade_in);
        mTextSwitcher.setOutAnimation(this, android.R.anim.fade_out);
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        mTextSwitcher.setText(title);
    }

    protected abstract int layoutId();
}
