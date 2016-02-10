package info.meizi_retrofit.ui.hot;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.Arrays;

import butterknife.Bind;
import info.meizi_retrofit.R;
import info.meizi_retrofit.ui.base.ToolBarActivity;
import me.gujun.android.taggroup.TagGroup;

/**
 * Created by Mr_Wrong on 16/1/10.
 */
public class HotActivity extends ToolBarActivity {
    @Bind(R.id.tag_group)
    TagGroup tagGroup;
    @Bind(R.id.girl_group)
    TagGroup girlGroup;

    String[] tags;
    String[] tagUrls;
    String[] girls;
    String[] girlUrls;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("热门");
        tags = getResources().getStringArray(R.array.tag);
        tagUrls = getResources().getStringArray(R.array.tagUrls);
        girls = getResources().getStringArray(R.array.girls);
        girlUrls = getResources().getStringArray(R.array.girlUrls);
        tagGroup.setTags(tags);
        girlGroup.setTags(girls);

        tagGroup.setOnTagClickListener(new TagGroup.OnTagClickListener() {
            @Override
            public void onTagClick(String tag) {
                startHotGroupActivity(tagUrls[Arrays.asList(tags).indexOf(tag)], tag);
            }
        });
        girlGroup.setOnTagClickListener(new TagGroup.OnTagClickListener() {
            @Override
            public void onTagClick(String tag) {
                startHotGroupActivity(girlUrls[Arrays.asList(girls).indexOf(tag)], tag);
            }
        });

        handleHint();

    }

    private void handleHint() {
        SharedPreferences share = getSharedPreferences("meizi", Context.MODE_PRIVATE);
        boolean hasShow = share.getBoolean("hasShow", false);
        if (!hasShow) {//第一次打开
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogStyle);
            builder.setTitle("FBI Warning");
            builder.setMessage(getResources().getString(R.string.hot_hint));
            builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
            SharedPreferences sharedPreferences = getSharedPreferences("meizi", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("hasShow", true);
            editor.commit();//提交修改
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.searchmenu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);

        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                startHotGroupActivity("search/" + query, query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void startHotGroupActivity(String tag, String title) {
        Intent intent = new Intent(HotActivity.this, HotGroupActivity.class);
        intent.putExtra(HotGroupActivity.TAG, tag);
        intent.putExtra(HotGroupActivity.TITLE, title);
        startActivity(intent);
    }

    @Override
    protected int layoutId() {
        return R.layout.hot;
    }
}
