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

import info.meizi.net.GroupService;
import info.meizi.adapter.GroupAdapter;
import info.meizi.base.BaseFragment;
import info.meizi.base.MyApp;
import info.meizi.bean.Content;
import info.meizi.ui.largepic.LargePicActivity;
import io.realm.Realm;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by Mr_Wrong on 15/9/17.
 * 首页进去的某个group页面
 */
public class GroupFragment extends BaseFragment {
    String groupid;
    MaterialDialog mMaterialDialog;
    GroupAdapter mAdapter;
    Realm realm;
    int currentcount;
    boolean isfrist = false;

    public GroupFragment(String groupid) {
        this.groupid = groupid;
    }

    private BroadcastReceiver Receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(groupid)) {
                int count = intent.getIntExtra("count", 0);

                currentcount = Content.all(realm, groupid).size();

                mMaterialDialog.setMessage(currentcount + "");

                //mMaterialDialog.show();

                if (currentcount == count) {
                    // mMaterialDialog.dismiss();
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
        if ( !isVisible) {
            return;
        }
        SendToLoad();
    }


    private void startLargePicActivity(View view, int position) {
        Intent intent1 = new Intent(getActivity(), LargePicActivity.class);
        intent1.putExtra("index", position);
        intent1.putExtra("groupid", groupid);
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(getActivity(), view, mAdapter.get(position).getUrl());
        getActivity().startActivity(intent1, options.toBundle());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        IntentFilter filter = new IntentFilter();
        filter.addAction(groupid);

        getActivity().registerReceiver(Receiver, filter);

        realm = Realm.getInstance(getActivity());
        mAdapter = new GroupAdapter(getContext()) {
            @Override
            protected void onItemClick(View v, int position) {
                startLargePicActivity(v, position);
            }
        };

        mRecyclerView.setAdapter(mAdapter);

        mMaterialDialog = new MaterialDialog(getContext())
                .setTitle("正在加载");
        SendToLoad();


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
        getActivity().unregisterReceiver(Receiver);
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = MyApp.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }
}
