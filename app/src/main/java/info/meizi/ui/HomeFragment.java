package info.meizi.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    private String path = "http://www.mzitu.com/";
    String groupid;

    public HomeFragment(String groupid) {
        this.groupid = groupid;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        isPrepared = true;
        return rootView;
    }

    private BroadcastReceiver uploadImgReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtils.e("收到广播了");
            if (intent.getAction().equals(groupid)) {
                List<TestContent> list = (ArrayList<TestContent>) intent.getSerializableExtra("list");
//                LogUtils.e(list.size());
                MeiziAdapter mAdapter = new MeiziAdapter(getContext(), list);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }

        }
    };


    @Override
    protected void lazyLoad() {
        LogUtils.e(isPrepared);
        LogUtils.e(isVisible);
        if (!isPrepared || !isVisible) {//这个判断要在子类进行
            return;
        }
        LogUtils.e("开始加载了");
        Intent intent = new Intent(getActivity(), FetchingService.class);
        intent.putExtra("groupid", groupid);
        getActivity().startService(intent);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        IntentFilter filter = new IntentFilter();
        filter.addAction(groupid);
        getActivity().registerReceiver(uploadImgReceiver, filter);
    }
}
