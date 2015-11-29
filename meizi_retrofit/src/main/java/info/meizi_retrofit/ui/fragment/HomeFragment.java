package info.meizi_retrofit.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;

import java.util.List;

import info.meizi_retrofit.R;
import info.meizi_retrofit.adapter.HomeAdapter;
import info.meizi_retrofit.base.BaseFragment;
import info.meizi_retrofit.model.Group;
import info.meizi_retrofit.net.ContentParser;
import info.meizi_retrofit.ui.GroupActivity;
import info.meizi_retrofit.utils.Utils;
import info.meizi_retrofit.widget.RadioImageView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Mr_Wrong on 15/10/30.
 */
public class HomeFragment extends BaseFragment {
    private HomeAdapter mAdapter;
    private String type;
    private int page = 2;
    private boolean hasload = false;

    public HomeFragment() {
    }

    public static HomeFragment newFragment(String type) {
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getString("type");
    }

    private void StartLoad(int page) {
        Utils.statrtRefresh(mRefresher, true);
        mSubscriptions.add(mGroupApi.getGroup(type, page).map(new Func1<String, List<Group>>() {
            @Override
            public List<Group> call(String s) {
                return ContentParser.ParserGroups(s, type);
            }
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Group>>() {
                    @Override
                    public void call(List<Group> groups) {
                        if (!hasload) {
                            mAdapter.replaceWith(groups);
                        } else {
                            mAdapter.addAll(groups);
                        }
                        hasload = false;
                        mRefresher.setRefreshing(false);
                    }
                }));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new HomeAdapter(getContext()) {
            @Override
            protected void onItemClick(View v, int position) {
                startGroupActivity(v, position);
            }
        };
        StartLoad(1);

        mRecyclerView.setAdapter(mAdapter);
    }

    private void startGroupActivity(View view, int position) {

        RadioImageView imageView = (RadioImageView) view.findViewById(R.id.iv_main_item);
        Bitmap bitmap = null;
        BitmapDrawable bd = (BitmapDrawable) imageView.getDrawable();
        if (bd != null) {
            bitmap = bd.getBitmap();
        }
        Intent intent1 = new Intent(getActivity(), GroupActivity.class);
        intent1.putExtra(GroupActivity.COLOR, Utils.getPaletteColor(bitmap));
        intent1.putExtra(GroupActivity.INDEX, position);
        intent1.putExtra(GroupActivity.GROUPID, Utils.url2groupid(mAdapter.get(position).getUrl()));
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(getActivity(), view, mAdapter.get(position).getUrl());
        getActivity().startActivity(intent1, options.toBundle());
    }

    @Override
    protected void loadMore() {
        if (hasload) {
            return;
        }
        StartLoad(page);
        page++;
        hasload = true;
    }

    @Override
    protected void lazyLoad() {
    }

    @Override
    public void onRefresh() {
        StartLoad(1);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);//这个在这是因为fragment替换的时候会重叠  还不知道怎么去解决
    }
}
