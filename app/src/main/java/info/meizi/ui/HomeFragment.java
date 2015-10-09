package info.meizi.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.SharedElementCallback;
import android.view.View;

import java.util.List;
import java.util.Map;

import info.meizi.FetchingService;
import info.meizi.adapter.MeiziAdapter;
import info.meizi.base.BaseFragment;
import info.meizi.bean.Content;
import info.meizi.utils.LogUtils;
import io.realm.Realm;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by Mr_Wrong on 15/9/17.
 */
public class HomeFragment extends BaseFragment {
    String groupid;
    MaterialDialog mMaterialDialog;
    MeiziAdapter mAdapter;
    private Bundle reenterState;
    public HomeFragment(String groupid) {
        this.groupid = groupid;

    }

    private BroadcastReceiver Receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtils.e("收到广播了,开始显示meizi");
            if (intent.getAction().equals(groupid)) {
                Realm realm = Realm.getInstance(getActivity());
                List<Content> latest =Content.all(realm,groupid);

                int count = intent.getIntExtra("count", 0);
                int currentcount = intent.getIntExtra("currentcount", 0);

                mMaterialDialog.setMessage(currentcount + "");
                mMaterialDialog.show();

                if (currentcount == count) {
                    mMaterialDialog.dismiss();
                    Snackbar.make(rootView,"精彩马上呈现",Snackbar.LENGTH_SHORT).show();
                }
                if (mAdapter == null) {
                    mAdapter = new MeiziAdapter(getContext(), latest);
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.setOnitemClickListener(new MeiziAdapter.OnitemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {

                            Intent intent1 = new Intent(getActivity(),LargePicActivity.class);
                            intent1.putExtra("index",position);
                            intent1.putExtra("groupid", groupid);
                            ActivityOptionsCompat options = ActivityOptionsCompat
                                    .makeSceneTransitionAnimation(getActivity(), view, mAdapter.get(position).getUrl());
                            getActivity().startActivity(intent1, options.toBundle());
                        }

                        @Override
                        public void onItemLongClick(View view, int pisition) {

                        }
                    });
                }
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }

        }
    };

    @Nullable
    @Override
    protected void lazyLoad() {
        if ( !isVisible) {
            return;
        }
        SendToLoad();
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        IntentFilter filter = new IntentFilter();
        filter.addAction(groupid);

       // getActivity().registerReceiver(Receiver, filter);


        mMaterialDialog = new MaterialDialog(getContext())
                .setTitle("正在加载");


        setExitSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                if (reenterState != null) {
                    int i = reenterState.getInt("index", 0);

                    sharedElements.clear();
                    sharedElements.put(mAdapter.get(i).getUrl(), layoutManager.findViewByPosition(i));

                    reenterState = null;
                }
            }
        });



    }

    /**
     * 发送信号去加载
     */
    private void SendToLoad() {
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
