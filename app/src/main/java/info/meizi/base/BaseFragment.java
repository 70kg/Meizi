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

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import info.meizi.R;
import info.meizi.adapter.MeiziAdapter;
import info.meizi.bean.Content;
import info.meizi.utils.LogUtils;

/**
 * Created by Mr_Wrong on 15/9/17.
 */
public class BaseFragment extends Fragment {
    public static RequestQueue mRequestQueue = Volley.newRequestQueue(MyApp.getContext());
    protected List<Content> mDatas = new ArrayList<Content>();
    protected MeiziAdapter mAdapter;
    protected View rootView;
    @Bind(R.id.id_recyclerview)
    protected RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new MeiziAdapter(getContext(), mDatas);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);
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