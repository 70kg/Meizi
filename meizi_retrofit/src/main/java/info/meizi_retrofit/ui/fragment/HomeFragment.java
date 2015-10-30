package info.meizi_retrofit.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import java.util.List;

import info.meizi_retrofit.adapter.HomeAdapter;
import info.meizi_retrofit.base.BaseFragment;
import info.meizi_retrofit.model.Group;
import info.meizi_retrofit.net.ContentParser;
import info.meizi_retrofit.utils.LogUtils;
import io.realm.Realm;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Mr_Wrong on 15/10/30.
 */
public class HomeFragment extends BaseFragment {
    private Realm realm;
    private HomeAdapter mAdapter;
    private String type;
    private int page = 2;
    private boolean hasload = false;

    public HomeFragment(String type) {
        this.type = type;
    }

    @Override
    protected void lazyLoad() {
        if (!isVisible) {
            return;
        }
    }

    private void StartLoad(String page) {
        mSubscriptions.add(mGroupApi.getGroup(type,page).map(new Func1<String, List<Group>>() {
                    @Override
                    public List<Group> call(String s) {
                        return ContentParser.ParserGroups(s, type);
                    }
                })
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<List<Group>>() {
                            @Override
                            public void call(List<Group> groups) {
                                LogUtils.e(groups.size());
                                if(!hasload){
                                    mAdapter.replaceWith(groups);
                                }else{
                                    mAdapter.addAll(groups);
                                }
                                hasload = false;

                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                LogUtils.e(throwable);
                            }
                        })


        );
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        realm = Realm.getInstance(getActivity());

        mAdapter = new HomeAdapter(getContext()) {
            @Override
            protected void onItemClick(View v, int position) {

            }
        };
        StartLoad("1");
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void loadMore() {
        if (hasload) {
            return;
        }
        StartLoad(page + "");
        page++;
        hasload = true;
    }

    @Override
    public void onRefresh() {

    }
}
