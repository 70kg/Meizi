package info.meizi.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.ArrayList;

import info.meizi.FetchingService;
import info.meizi.adapter.MeiziAdapter;
import info.meizi.base.BaseFragment;
import info.meizi.bean.TestContent;
import info.meizi.utils.LogUtils;
import io.realm.Realm;
import io.realm.RealmResults;

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
                ArrayList<TestContent> list = (ArrayList<TestContent>) intent.getSerializableExtra("list");

                Realm realm = Realm.getInstance(getActivity());
                RealmResults<TestContent> latest = realm.where(TestContent.class)
                        .findAllSorted("order", RealmResults.SORT_ORDER_DESCENDING);

                MeiziAdapter mAdapter = new MeiziAdapter(getContext(), latest);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }

        }
    };

    @Nullable
    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
        SendToLoad();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        IntentFilter filter = new IntentFilter();
        filter.addAction(groupid);
        LogUtils.e("IntentFilter的action" + groupid);
        getActivity().registerReceiver(Receiver, filter);
    }

    /**
     * 发送信号去加载
     */
    private void SendToLoad() {
        LogUtils.e("给intentservice发送加载命令");
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
