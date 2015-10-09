package info.meizi.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import info.meizi.R;
import info.meizi.utils.LogUtils;

/**
 * Created by Mr_Wrong on 15/9/17.
 */
public abstract class BaseFragment extends Fragment {
    protected View rootView;
    @Bind(R.id.id_recyclerview)
    protected RecyclerView mRecyclerView;
    protected boolean isPrepared;
    protected boolean isVisible;
    private boolean hasload;
    protected StaggeredGridLayoutManager layoutManager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.id_recyclerview);
        ButterKnife.bind(this, rootView);
        isPrepared= true;
        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
            LogUtils.e(isPrepared + "--" + isVisible);
        } else {
            isVisible = false;
            onInvisible();
        }


    }

    protected void onVisible() {
        if (!hasload) {
            lazyLoad();
            hasload = true;
        }
    }

    protected abstract void lazyLoad();

    protected void onInvisible() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Picasso.with(getContext()).resumeTag("1");
                } else {
                    Picasso.with(getContext()).pauseTag("1");
                }
            }
        });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}