package info.meizi.ui.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;

import java.util.List;

import info.meizi.R;
import info.meizi.adapter.MainAdapter;
import info.meizi.base.BaseFragment;
import info.meizi.bean.MainBean;
import info.meizi.net.MainService;
import info.meizi.ui.group.GroupActivity;
import info.meizi.utils.LogUtils;
import info.meizi.utils.Utils;
import info.meizi.widget.RadioImageView;
import io.realm.Realm;

/**
 * Created by Mr_Wrong on 15/10/9.
 * 首页那几个tab
 */
public class MainFragment extends BaseFragment {
    private String type;
    private MainAdapter mAdapter;

    public MainFragment(String type) {
        this.type = type;
    }

    private int page = 2;
    private boolean hasload = false;
    private Realm realm;
    private Handler handler = new Handler();
    private BroadcastReceiver Receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(type)) {
                boolean isRefreshe, isLoadmore, isFirstload;
                List<MainBean> latest = MainBean.all(realm, type);
                hasload = false;

                isRefreshe = intent.getBooleanExtra("isRefreshe", false);
                isLoadmore = intent.getBooleanExtra("isLoadmore", false);
                isFirstload = intent.getBooleanExtra("isFirstload", false);

                if (isFirstload||isRefreshe) {
                    mAdapter.replaceWith(latest);
                }
                if (isLoadmore) {
                    mAdapter.addAll(latest.subList(latest.size() - 24, latest.size()));
                }
                mRefresher.setRefreshing(false);
            }
        }
    };

    @Override
    protected void lazyLoad() {
        if (!isVisible) {
            return;
        }
        SendToLoad("");
    }

    //发送去加载首页数据        就是page页数
    private void SendToLoad(String page) {
        //这个是防止首页空指针 还没想到处理的好办法
        handler.post(new Runnable() {
            @Override
            public void run() {
                mRefresher.setRefreshing(true);
            }
        });
        Intent intent = new Intent(getActivity(), MainService.class);
        intent.putExtra("type", type);
        intent.putExtra("page", page);
        getActivity().startService(intent);
    }

    private void startGroupActivity(View view, int position) {

        RadioImageView imageView = (RadioImageView) view.findViewById(R.id.iv_main_item);
        Bitmap bitmap = null;
        BitmapDrawable bd = (BitmapDrawable) imageView.getDrawable();
        if (bd != null) {
            bitmap = bd.getBitmap();
        }
        Intent intent1 = new Intent(getActivity(), GroupActivity.class);
        intent1.putExtra("color", Utils.getPaletteColor(bitmap));
        intent1.putExtra("index", position);
        intent1.putExtra("groupid", Utils.url2groupid(mAdapter.get(position).getUrl()));
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(getActivity(), view, mAdapter.get(position).getUrl());
        getActivity().startActivity(intent1, options.toBundle());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        IntentFilter filter = new IntentFilter();
        filter.addAction(type);
        getActivity().registerReceiver(Receiver, filter);

        realm = Realm.getInstance(getActivity());

        mAdapter = new MainAdapter(getContext()) {
            @Override
            protected void onItemClick(View v, int position) {
                startGroupActivity(v, position);
            }
        };

        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void loadMore() {
        if (hasload) {
            return;
        }
        SendToLoad(page + "");
        LogUtils.d("发送加载更多  " + type + "/page/" + page);
        page++;
        hasload = true;
    }


    @Override
    public void onDestroyView() {
        getActivity().unregisterReceiver(Receiver);
        super.onDestroyView();
    }

    //刷新的回调
    @Override
    public void onRefresh() {
        SendToLoad("");
    }
}
