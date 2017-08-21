package info.meizi_retrofit.ui.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;

import com.socks.library.KLog;

import java.util.List;

import info.meizi_retrofit.R;
import info.meizi_retrofit.adapter.HomeAdapter;
import info.meizi_retrofit.model.Group;
import info.meizi_retrofit.net.Api;
import info.meizi_retrofit.net.ContentParser;
import info.meizi_retrofit.net.GroupApi;
import info.meizi_retrofit.ui.base.BaseFragment;
import info.meizi_retrofit.ui.group.GroupActivity;
import info.meizi_retrofit.utils.Utils;
import info.meizi_retrofit.widget.RadioImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observer;
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
    protected GroupApi mGroupApi;

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
        mGroupApi = createGroupApi();
    }

    private void StartLoad(int page, boolean isrefresh) {
        if (page == 1 && !isrefresh)//只有第一次进去才加载
            mAdapter.addAll(Group.all(realm, type));
        Utils.statrtRefresh(mRefresher, true);
        KLog.e("http://www.mzitu.com/" + type + "/page/" + page);
        mGroupApi.getGroupWithCall("http://www.mzitu.com/" + type + "/page/" + page)
                .map(new Func1<String, List<Group>>() {
                    @Override
                    public List<Group> call(String s) {
                        return ContentParser.ParserGroups(s, type);
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Action1<List<Group>>() {
                    @Override
                    public void call(List<Group> groups) {
                        saveDb(groups);
                    }
                })
                .subscribe(new listObserver());
    }

    private class listObserver implements Observer<List<Group>> {

        @Override
        public void onCompleted() {
            mRefresher.setRefreshing(false);
        }

        @Override
        public void onError(Throwable e) {
            if (e.getMessage().equals("404 Not Found")) {
                Snackbar.make(mRecyclerView, "没有更多啦", Snackbar.LENGTH_SHORT).show();
            } else {
                Snackbar.make(mRecyclerView, "出现错误啦", Snackbar.LENGTH_SHORT).show();
            }
            Utils.statrtRefresh(mRefresher, false);
            KLog.e(e);
        }

        @Override
        public void onNext(List<Group> groups) {
            if (!hasload) {
                mAdapter.replaceWith(groups);
            } else {
                mAdapter.addAll(groups);
            }
            hasload = false;
            mRefresher.setRefreshing(false);
        }
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
        StartLoad(1, false);

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
        if (bitmap != null && !bitmap.isRecycled()) {
            intent1.putExtra(GroupActivity.COLOR, Utils.getPaletteColor(bitmap));
        }
        intent1.putExtra("title", mAdapter.get(position).getTitle());
        //现在只需要传递处理完的URL进去
        intent1.putExtra("url", mAdapter.get(position).getImageurl());
        intent1.putExtra(GroupActivity.GROUPID, Utils.url2groupid(mAdapter.get(position).getUrl()));
        if (bitmap != null) {
            ActivityOptionsCompat options = ActivityOptionsCompat.makeThumbnailScaleUpAnimation(view, bitmap, 0, 0);
            ActivityCompat.startActivity(getActivity(), intent1, options.toBundle());
        } else {
            startActivity(intent1);
        }

    }

    @Override
    protected void loadMore() {
        if (hasload) {
            return;
        }
        page = mAdapter.getItemCount() / 24;
        page = page < 2 ? 2 : page;
        StartLoad(page, false);
        page++;
        hasload = true;
    }

    @Override
    protected void lazyLoad() {
    }

    @Override
    public void onRefresh() {
        mRefresher.setRefreshing(false);
    }

    protected GroupApi createGroupApi() {
        return Api.getInsatcne().createGroupApi();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);//这个在这是因为fragment替换的时候会重叠  还不知道怎么去解决
    }
}
