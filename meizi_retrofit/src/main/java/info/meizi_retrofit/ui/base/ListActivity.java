package info.meizi_retrofit.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import butterknife.Bind;
import info.meizi_retrofit.R;
import info.meizi_retrofit.widget.MySwipeRefreshLayout;

/**
 * Created by Mr_Wrong on 16/1/6.
 */
public abstract class ListActivity extends ToolBarActivity implements SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.abs_recyclerview)
    protected RecyclerView mRecyclerView;
    @Bind(R.id.abs_refresher)
    protected MySwipeRefreshLayout mRefresher;
    protected StaggeredGridLayoutManager mLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRefresher.setOnRefreshListener(this);
        mRefresher.setColorSchemeColors(mColor);
    }
    @Override
    protected int layoutId() {
        return R.layout.abs_list_layout;
    }
}
