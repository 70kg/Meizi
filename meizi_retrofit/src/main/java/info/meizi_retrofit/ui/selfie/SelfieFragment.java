package info.meizi_retrofit.ui.selfie;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;

import com.socks.library.KLog;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import info.meizi_retrofit.adapter.SelfieAdapter;
import info.meizi_retrofit.model.Selfie;
import info.meizi_retrofit.net.Api;
import info.meizi_retrofit.net.SelfieApi;
import info.meizi_retrofit.ui.base.BaseFragment;
import info.meizi_retrofit.ui.largepic.LargePicActivity;
import info.meizi_retrofit.utils.Utils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Mr_Wrong on 16/1/26.
 */
public class SelfieFragment extends BaseFragment {
    private SelfieApi mApi;
    public SelfieAdapter mAdapter;
    public RecyclerView.LayoutManager mLayoutManager;
    private int mPage;

    private boolean hasload = true;
    private ArrayList<String> urls = new ArrayList<>();

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

    private void startLoad(int page) {
        Utils.statrtRefresh(mRefresher, true);
        mSubscriptions.add(mApi.getSelfie("http://www.mzitu.com/zipai/comment-page-" + page + "/")
                .map(new Func1<String, Elements>() {
                    @Override
                    public Elements call(String s) {
                        return Jsoup.parse(s).select("p:has(img)");
                    }
                })
                .flatMap(new Func1<Elements, Observable<String>>() {
                    @Override
                    public Observable<String> call(final Elements elements) {
                        return Observable.range(0, elements.size())
                                .map(new Func1<Integer, String>() {
                                    @Override
                                    public String call(Integer integer) {
                                        return elements.get(integer).select("img").first().attr("src");
                                    }
                                });
                    }
                })
                .map(new Func1<String, Selfie>() {
                    @Override
                    public Selfie call(String url) {
                        return handleSelfie(url);
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Selfie>() {
                    @Override
                    public void onCompleted() {
                        hasload = false;
                        Utils.statrtRefresh(mRefresher, false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e(e);
                    }

                    @Override
                    public void onNext(Selfie selfie) {
                        urls.add(selfie.getUrl());
                        mAdapter.add(selfie);
                    }
                }));
    }


    public void scrollToPosition(int index) {
        mRecyclerView.scrollToPosition(index);
        mRecyclerView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mRecyclerView.getViewTreeObserver().removeOnPreDrawListener(this);
                mRecyclerView.requestLayout();
                getActivity().supportStartPostponedEnterTransition();
                return true;
            }
        });
    }

    private Selfie handleSelfie(String url) {
        Selfie selfie = new Selfie();
        selfie.setUrl(url);
        try {
            Response response;
            response = new OkHttpClient().newCall(new Request.Builder().url(url).build()).execute();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(response.body().byteStream(), null, options);
            selfie.setWidth(options.outWidth);
            selfie.setHeight(options.outHeight);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return selfie;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mLayoutManager = mRecyclerView.getLayoutManager();
        mAdapter = new SelfieAdapter(getContext()) {
            @Override
            protected void onItemClick(View v, int position) {
                startLargPicActivity(v, position);
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        mApi.getSelfie("http://www.mzitu.com/zipai/")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        mPage = Integer.parseInt(Jsoup.parse(s).select("span.current").first().text());
                        startLoad(mPage);
                    }
                });
    }

    private void startLargPicActivity(View view, int position) {
        Intent intent = new Intent(getContext(), LargePicActivity.class);
        intent.putExtra(LargePicActivity.INDEX, position);
        intent.putExtra(LargePicActivity.GROUPID, new Date().getTime() + "");
        intent.putExtra(LargePicActivity.URLS, urls);

        if (Build.VERSION.SDK_INT >= 22) {
            ActivityOptionsCompat options = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(getActivity(), view, mAdapter.get(position).getUrl());
            getActivity().startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }


    private SelfieApi createApi() {
        return Api.getInsatcne().createSelfieApi();
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected void loadMore() {
        if (hasload) {
            return;
        }
        startLoad(--mPage);
        hasload = true;
    }

    @Override
    public void onRefresh() {
        mRefresher.setRefreshing(false);
    }
}
