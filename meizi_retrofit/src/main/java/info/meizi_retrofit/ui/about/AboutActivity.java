package info.meizi_retrofit.ui.about;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import butterknife.Bind;
import info.meizi_retrofit.R;
import info.meizi_retrofit.ui.base.ToolBarActivity;
import info.meizi_retrofit.widget.PullScrollView;

/**
 * Created by Mr_Wrong on 15/11/29.
 */
public class AboutActivity extends ToolBarActivity {

    @Bind(R.id.background_img)
    ImageView backgroundImg;
    @Bind(R.id.scroll_view)
    PullScrollView scrollView;

    public void _70kg(View view) {
        Intent intent = new Intent(AboutActivity.this, WebActivity.class);
        intent.putExtra(WebActivity.TITLE, "Github");
        intent.putExtra(WebActivity.URL, "https://github.com/70kg/Meizi");
        startActivity(intent);
    }

    public void _weibo(View view) {
        Intent intent = new Intent(AboutActivity.this, WebActivity.class);
        intent.putExtra(WebActivity.TITLE, "WeiBo");
        intent.putExtra(WebActivity.URL, "http://weibo.com/MrWrong12138?is_all=1");
        startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scrollView.setHeader(backgroundImg);
        setTitle("关于");
    }

    @Override
    protected int layoutId() {
        return R.layout.about;




    }
}
