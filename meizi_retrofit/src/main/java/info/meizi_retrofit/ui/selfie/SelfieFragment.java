package info.meizi_retrofit.ui.selfie;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.socks.library.KLog;

import info.meizi_retrofit.base.BaseFragment;
import info.meizi_retrofit.net.SelfieApi;
import info.meizi_retrofit.net.SelfieParse;
import info.meizi_retrofit.utils.StringConverter;
import info.meizi_retrofit.utils.Utils;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Mr_Wrong on 16/1/26.
 */
public class SelfieFragment extends BaseFragment {
    private SelfieApi mApi;

    public SelfieFragment() {
    }

    public static SelfieFragment newFragment() {
        return new SelfieFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApi = createApi();
    }

    private void StartLoad(String page) {
//        Utils.statrtRefresh(mRefresher, true);
        mSubscriptions.add(mApi.getSelfie(page)
                .map(new Func1<String, Integer>() {
                    @Override
                    public Integer call(String s) {
                        return SelfieParse.getPage(s);
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onCompleted() {
                        Utils.statrtRefresh(mRefresher, false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e(e);
                    }

                    @Override
                    public void onNext(Integer s) {
//                        KLog.e(s);
                    }
                }));


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        StartLoad("comment-page-190");
    }


    private SelfieApi createApi() {
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint("http://www.mzitu.com/share/")
                .setConverter(new StringConverter())
                .setClient(new OkClient())
                .build();
        return adapter.create(SelfieApi.class);
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected void loadMore() {

    }

    @Override
    public void onRefresh() {

    }
}
