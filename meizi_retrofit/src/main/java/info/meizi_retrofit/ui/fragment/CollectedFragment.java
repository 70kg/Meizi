package info.meizi_retrofit.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import java.util.List;

import info.meizi_retrofit.R;
import info.meizi_retrofit.adapter.CollectedAdapter;
import info.meizi_retrofit.base.BaseFragment;
import info.meizi_retrofit.model.WrapGroup;
import info.meizi_retrofit.ui.GroupActivity;
import info.meizi_retrofit.utils.LogUtils;
import info.meizi_retrofit.utils.Utils;
import info.meizi_retrofit.widget.RadioImageView;

/**
 * Created by Mr_Wrong on 15/12/1.
 */
public class CollectedFragment extends BaseFragment {
    private CollectedAdapter mAdapter;

    CollectedFragment() {
    }

    public static CollectedFragment newFragment() {
        Bundle bundle = new Bundle();
        CollectedFragment fragment = new CollectedFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected void loadMore() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        List<WrapGroup> groups = WrapGroup.all(realm);
        LogUtils.e("收藏的个数：" + groups.size());
        mAdapter = new CollectedAdapter(getContext()) {
            @Override
            protected void onItemClick(View v, int position) {
                startGroupActivity(v, position);
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.replaceWith(groups);
    }

    private void startGroupActivity(View view, int position) {

        RadioImageView imageView = (RadioImageView) view.findViewById(R.id.iv_main_item);
        Bitmap bitmap = null;
        BitmapDrawable bd = (BitmapDrawable) imageView.getDrawable();
        if (bd != null) {
            bitmap = bd.getBitmap();
        }

        Intent intent1 = new Intent(getActivity(), GroupActivity.class);
        intent1.putExtra("group", mAdapter.get(position));
        intent1.putExtra(GroupActivity.COLOR, Utils.getPaletteColor(bitmap));
        intent1.putExtra(GroupActivity.INDEX, position);
        intent1.putExtra(GroupActivity.GROUPID, Utils.url2groupid(mAdapter.get(position).getGroup().getUrl()));
        getActivity().startActivity(intent1);
    }

    @Override
    public void onResume() {
        super.onResume();
        List<WrapGroup> groups = WrapGroup.all(realm);
        mAdapter.replaceWith(groups);
    }

    @Override
    public void onRefresh() {
        List<WrapGroup> groups = WrapGroup.all(realm);
        mAdapter.replaceWith(groups);
        mRefresher.setRefreshing(false);
    }

}
