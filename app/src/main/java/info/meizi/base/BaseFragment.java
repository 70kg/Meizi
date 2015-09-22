package info.meizi.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import butterknife.Bind;
import butterknife.ButterKnife;
import info.meizi.R;
import info.meizi.utils.LogUtils;

/**
 * Created by Mr_Wrong on 15/9/17.
 */
public abstract class BaseFragment extends Fragment {
    public static RequestQueue mRequestQueue = Volley.newRequestQueue(MyApp.getContext());
    protected View rootView;
    @Bind(R.id.id_recyclerview)
    protected RecyclerView mRecyclerView;
    protected boolean isPrepared;//控件等是否已经加载完
    protected boolean isVisible;//是否可见
    private boolean hasload;//是否已经记载过  getUserVisibleHint会在fragment显示或隐藏时重复调
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.id_recyclerview);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }

    }
    protected void onVisible(){//可见 只加载一次
        if(!hasload){
            lazyLoad();
            hasload = true;
        }
    }
    protected abstract void lazyLoad();
    protected void onInvisible(){}

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
    }

    protected void executeRequest(Request<?> request) {
        if (this != null) {
            request.setTag(this);
        }
        mRequestQueue.add(request);
        LogUtils.d("addRequest = " + request.getUrl());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}