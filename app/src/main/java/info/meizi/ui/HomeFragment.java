package info.meizi.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import info.meizi.FetchingService;
import info.meizi.adapter.MeiziAdapter;
import info.meizi.base.BaseFragment;
import info.meizi.bean.TestContent;
import info.meizi.utils.LogUtils;

/**
 * Created by Mr_Wrong on 15/9/17.
 */
public class HomeFragment extends BaseFragment {
    String groupid;
    public HomeFragment(String groupid) {
        this.groupid = groupid;
    }

    private BroadcastReceiver Receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtils.e("收到广播了,开始显示meizi");
            if (intent.getAction().equals(groupid)) {
                List<TestContent> list = (ArrayList<TestContent>) intent.getSerializableExtra("list");
                MeiziAdapter mAdapter = new MeiziAdapter(getContext(), list);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }

        }
    };
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        IntentFilter filter = new IntentFilter();
        filter.addAction(groupid);
        getActivity().registerReceiver(Receiver, filter);
    }
    @Nullable
    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
        SendToLoad();
    }

    /**
     * 发送信号去加载
     */
    private void SendToLoad() {
        LogUtils.e("开始加载了");
        Intent intent = new Intent(getActivity(), FetchingService.class);
        intent.putExtra("groupid", groupid);
        getActivity().startService(intent);
    }

    @Override
    public void onDestroyView() {
        getActivity().unregisterReceiver(Receiver);
        super.onDestroyView();
    }
}
