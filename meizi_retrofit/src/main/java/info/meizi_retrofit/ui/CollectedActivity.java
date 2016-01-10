package info.meizi_retrofit.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import java.util.List;

import info.meizi_retrofit.R;
import info.meizi_retrofit.adapter.CollectedAdapter;
import info.meizi_retrofit.model.WrapGroup;
import info.meizi_retrofit.ui.base.ListActivity;
import info.meizi_retrofit.ui.group.GroupActivity;
import info.meizi_retrofit.utils.LogUtils;
import info.meizi_retrofit.utils.Utils;
import info.meizi_retrofit.widget.RadioImageView;

/**
 * Created by Mr_Wrong on 16/1/6.
 */
public class CollectedActivity extends ListActivity {
    private CollectedAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<WrapGroup> groups = WrapGroup.all(realm);
        LogUtils.e("收藏的个数：" + groups.size());
        mAdapter = new CollectedAdapter(this) {
            @Override
            protected void onItemClick(View v, int position) {
                startGroupActivity(v, position);
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.replaceWith(groups);
        setTitle("我的收藏");
    }

    private void startGroupActivity(View view, int position) {

        RadioImageView imageView = (RadioImageView) view.findViewById(R.id.iv_main_item);
        Bitmap bitmap = null;
        BitmapDrawable bd = (BitmapDrawable) imageView.getDrawable();
        if (bd != null) {
            bitmap = bd.getBitmap();
        }

        Intent intent1 = new Intent(this, GroupActivity.class);
        if (bitmap != null && !bitmap.isRecycled()) {
            intent1.putExtra(GroupActivity.COLOR, Utils.getPaletteColor(bitmap));
        }
        intent1.putExtra("title", mAdapter.get(position).getGroup().getTitle());
        intent1.putExtra("url", mAdapter.get(position).getGroup().getImageurl());
        intent1.putExtra(GroupActivity.GROUPID, Utils.url2groupid(mAdapter.get(position).getGroup().getUrl()));
        startActivity(intent1);
    }

    @Override
    public void onRefresh() {
        List<WrapGroup> groups = WrapGroup.all(realm);
        mAdapter.replaceWith(groups);
        mRefresher.setRefreshing(false);
    }

}
