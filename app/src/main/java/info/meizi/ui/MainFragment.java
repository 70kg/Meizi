package info.meizi.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.List;

import info.meizi.MainService;
import info.meizi.adapter.MainAdapter;
import info.meizi.base.BaseFragment;
import info.meizi.bean.MainBean;
import info.meizi.utils.LogUtils;
import io.realm.Realm;

/**
 * Created by Mr_Wrong on 15/10/9.
 */
public class MainFragment extends BaseFragment {
    String type;
    MainAdapter mAdapter;
    public MainFragment(String type) {
        this.type = type;
    }

    private BroadcastReceiver Receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtils.e("接受到广播");
            if (intent.getAction().equals(type)) {
                Realm realm = Realm.getInstance(getActivity());
                List<MainBean> latest = MainBean.all(realm,type);

                LogUtils.e(latest.get(0).getTitle());


                if (mAdapter == null) {
                    mAdapter = new MainAdapter(getContext(), latest);
                    mRecyclerView.setAdapter(mAdapter);
                }
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void lazyLoad() {
        if (!isVisible) {
            return;
        }
        SendToLoad();
    }

    //发送去加载首页数据
    private void SendToLoad() {
        Intent intent = new Intent(getActivity(), MainService.class);
        intent.putExtra("type", type);
        getActivity().startService(intent);
        LogUtils.e("发送加载数据");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        IntentFilter filter = new IntentFilter();
        filter.addAction(type);
        getActivity().registerReceiver(Receiver, filter);
    }
    @Override
    public void onDestroyView() {
        getActivity().unregisterReceiver(Receiver);
        super.onDestroyView();
    }

}
