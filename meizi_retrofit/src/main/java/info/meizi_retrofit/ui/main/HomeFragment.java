package info.meizi_retrofit.ui.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.socks.library.KLog;

import java.util.List;

import info.meizi_retrofit.R;
import info.meizi_retrofit.adapter.HomeAdapter;
import info.meizi_retrofit.base.BaseFragment;
import info.meizi_retrofit.model.Group;
import info.meizi_retrofit.net.ContentParser;
import info.meizi_retrofit.ui.group.GroupActivity;
import info.meizi_retrofit.utils.Utils;
import info.meizi_retrofit.widget.RadioImageView;
import io.realm.Realm;
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

    String currentImageUrl;

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
        currentImageUrl = Group.getFirstImageUrl(realm, type);

    }

    private void StartLoad(int page, boolean isrefresh) {
        if (page == 1 && !isrefresh)//只有第一次进去才加载
            mAdapter.addAll(Group.all(realm, type));

        Utils.statrtRefresh(mRefresher, true);
        KLog.e("http://www.mzitu.com/" + type + "/page/" + page);
        mSubscriptions.add(mGroupApi.getGroup(type, page).map(new Func1<String, List<Group>>() {
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
                        saveDb(groups, realm);
                    }
                })
//                .flatMap(new Func1<List<Group>, Observable<Group>>() {
//                    @Override
//                    public Observable<Group> call(List<Group> groups) {
//                        return Observable.from(groups);
//                    }
//                })
//                .subscribe(new groupObserver()));
                .subscribe(new listObserver()));
    }


    class groupObserver implements Observer<Group> {

        @Override
        public void onCompleted() {
            //mIndex = 0;
            hasload = false;
            Utils.statrtRefresh(mRefresher, false);
        }

        @Override
        public void onError(Throwable e) {
            KLog.e(e);
            Utils.statrtRefresh(mRefresher, false);
        }

        @Override
        public void onNext(Group group) {//这里的加载更多有问题
            //数据库不空  并且和数据库第一个不相等 就是新增的
            if (!currentImageUrl.isEmpty() && !currentImageUrl.equals(group.getImageurl())) {
                mAdapter.add(mIndex++, group);
            } else if (currentImageUrl.isEmpty()) {//数据库没东西 或者加载更多
                mAdapter.add(group);
            }

        }
    }

    int mIndex;//新添加的index

    class listObserver implements Observer<List<Group>> {

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            if (e.getMessage().equals("404 Not Found")) {
                Snackbar.make(mRecyclerView, "没有更多啦", Snackbar.LENGTH_SHORT).show();
            } else {
                Snackbar.make(mRecyclerView, "出现错误啦", Snackbar.LENGTH_SHORT).show();
            }
            mRefresher.setRefreshing(false);
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


    private void saveDb(List<Group> groups, Realm realm) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(groups);
        realm.commitTransaction();

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
        getActivity().startActivity(intent1);
    }

    @Override
    protected void loadMore() {
        if (hasload) {
            return;
        }
        page = mAdapter.getItemCount() / 24;
        page = page < 2 ? 2 : page;
        KLog.e(page);
        StartLoad(page, false);
        page++;
        hasload = true;
    }

    @Override
    protected void lazyLoad() {
    }

    @Override
    public void onRefresh() {
//        StartLoad(1, true);
        mRefresher.setRefreshing(false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);//这个在这是因为fragment替换的时候会重叠  还不知道怎么去解决
    }
}
