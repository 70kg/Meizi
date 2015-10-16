package info.meizi.ui.group;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;

import com.squareup.leakcanary.RefWatcher;

import info.meizi.adapter.GroupAdapter;
import info.meizi.base.BaseFragment;
import info.meizi.base.MyApp;
import info.meizi.bean.Content;
import info.meizi.net.GroupService;
import info.meizi.ui.largepic.LargePicActivity;
import io.realm.Realm;

/**
 * Created by Mr_Wrong on 15/9/17.
 * 首页进去的某个group页面
 */
public class GroupFragment extends BaseFragment {
    private String groupid;
    private GroupAdapter mAdapter;
    private Realm realm;
    private int currentcount;
    private boolean isfrist = false;
    private GroupActivity activity;

    public GroupFragment(String groupid) {
        this.groupid = groupid;
    }

    private BroadcastReceiver Receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(groupid)) {
                int count = intent.getIntExtra("count", 0);

                currentcount = Content.all(realm, groupid).size();

                mRefresher.setRefreshing(true);

                if (currentcount == count || count == 0) {
                    mRefresher.setRefreshing(false);
                    Snackbar.make(rootView, "精彩马上呈现", Snackbar.LENGTH_SHORT).show();
                }
                if (currentcount == 0) {//数据库空 第一次网络加载
                    isfrist = true;
                }
                if (isfrist) {
                    mAdapter.notifyItemInserted(currentcount);
                }
            }
            mAdapter.replaceWith(Content.all(realm, groupid));
        }
    };

    @Nullable
    @Override
    protected void lazyLoad() {
        if (!isVisible) {
            return;
        }
        SendToLoad();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (GroupActivity) context;
    }

    //这里有问题  好像不是在这里进行回调的。。
//    @Override
//    public void onResume() {
//        super.onResume();
//        int index = activity.getIndex();
//        if (index != -1) {
//            mRecyclerView.scrollToPosition(index);
//            mRecyclerView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//                @Override
//                public boolean onPreDraw() {
//                    mRecyclerView.getViewTreeObserver().removeOnPreDrawListener(this);
//                    mRecyclerView.requestLayout();
//                    activity.supportStartPostponedEnterTransition();
//                    return true;
//                }
//            });
//        }
//    }

    private void startLargePicActivity(View view, int position) {
        Intent intent1 = new Intent(activity, LargePicActivity.class);
        intent1.putExtra("index", position);
        intent1.putExtra("groupid", groupid);
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(getActivity(), view, mAdapter.get(position).getUrl());
        activity.startActivity(intent1, options.toBundle());
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        IntentFilter filter = new IntentFilter();
        filter.addAction(groupid);

        activity.registerReceiver(Receiver, filter);

        realm = Realm.getInstance(activity);
        mAdapter = new GroupAdapter(MyApp.getContext()) {
            @Override
            protected void onItemClick(View v, int position) {
                startLargePicActivity(v, position);
            }
        };

        mRecyclerView.setAdapter(mAdapter);
        mRefresher.setColorSchemeColors(activity.color);
        SendToLoad();

    }

    @Override
    protected void loadMore() {

    }

    /**
     * 发送信号去加载
     */
    private void SendToLoad() {
        Intent intent = new Intent(getActivity(), GroupService.class);
        intent.putExtra("groupid", groupid);
        getActivity().startService(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        rootView = null;
        activity.unregisterReceiver(Receiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
        RefWatcher refWatcher = MyApp.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }


    @Override
    public void onRefresh() {

    }
}
