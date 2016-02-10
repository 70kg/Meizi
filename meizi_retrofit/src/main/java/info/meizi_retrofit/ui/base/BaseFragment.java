package info.meizi_retrofit.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import info.meizi_retrofit.R;
import info.meizi_retrofit.model.SaveDb;
import info.meizi_retrofit.widget.MySwipeRefreshLayout;
import io.realm.Realm;
import io.realm.RealmObject;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Mr_Wrong on 15/10/30.
 */
public abstract class BaseFragment<T extends RealmObject> extends Fragment implements SwipeRefreshLayout.OnRefreshListener, SaveDb<T> {
    protected View rootView;
    @Bind(R.id.id_recyclerview)
    protected RecyclerView mRecyclerView;
    protected boolean isPrepared;
    protected boolean isVisible;
    protected Realm realm;
    @Bind(R.id.refresher)
    protected MySwipeRefreshLayout mRefresher;

    private boolean hasload;
    protected StaggeredGridLayoutManager layoutManager;
    protected CompositeSubscription mSubscriptions = new CompositeSubscription();//这个是持有订阅  用于生命周期

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.basefragment, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.id_recyclerview);
        mRefresher = (MySwipeRefreshLayout) rootView.findViewById(R.id.refresher);
        mRefresher.setColorSchemeResources(R.color.app_primary_color);
        mRefresher.setOnRefreshListener(this);
        ButterKnife.bind(this, rootView);
        isPrepared = true;
        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
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

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int[] positions = new int[layoutManager.getSpanCount()];
                layoutManager.findLastVisibleItemPositions(positions);
                int position = Math.max(positions[0], positions[1]);
                if (position == layoutManager.getItemCount() - 1) {
                    loadMore();
                }
            }
        });

    }

    protected abstract void loadMore();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.mSubscriptions != null) {
            this.mSubscriptions.unsubscribe();
        }
    }

    @Override
    public void saveDb(T t) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(t);
        realm.commitTransaction();
    }

    @Override
    public void saveDb(Iterable<T> objects) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(objects);
        realm.commitTransaction();
    }
}