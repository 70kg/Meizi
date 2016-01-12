package info.meizi_retrofit.ui.hot;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.Arrays;

import butterknife.Bind;
import info.meizi_retrofit.R;
import info.meizi_retrofit.ui.base.ToolBarActivity;
import info.meizi_retrofit.ui.hot.girl.HotGirlGroupActivity;
import info.meizi_retrofit.ui.hot.tag.TagGroupActivity;
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

                Arrays.asList(tags).indexOf(tag);

                Intent intent = new Intent(HotActivity.this, TagGroupActivity.class);
                intent.putExtra(TagGroupActivity.TAG, tagUrls[Arrays.asList(tags).indexOf(tag)]);
                intent.putExtra(TagGroupActivity.TITLE, tag);
                startActivity(intent);
            }
        });
        girlGroup.setOnTagClickListener(new TagGroup.OnTagClickListener() {
            @Override
            public void onTagClick(String tag) {
                Intent intent = new Intent(HotActivity.this, HotGirlGroupActivity.class);
                intent.putExtra(HotGirlGroupActivity.TAG, girlUrls[Arrays.asList(girls).indexOf(tag)]);
                intent.putExtra(HotGirlGroupActivity.TITLE, tag);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.searchmenu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);//在菜单中找到对应控件的item
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);

        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HotActivity.this, "点击了", Toast.LENGTH_SHORT).show();

            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(HotActivity.this, query, Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected int layoutId() {
        return R.layout.hot;
    }
}
