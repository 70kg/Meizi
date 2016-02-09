package info.meizi_retrofit.ui.selfie;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.SharedElementCallback;
import android.view.View;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;
import com.socks.library.KLog;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import info.meizi_retrofit.adapter.SelfieAdapter;
import info.meizi_retrofit.base.BaseFragment;
import info.meizi_retrofit.model.Selfie;
import info.meizi_retrofit.net.SelfieApi;
import info.meizi_retrofit.ui.largepic.LargePicActivity;
import info.meizi_retrofit.utils.StringConverter;
import info.meizi_retrofit.utils.Utils;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Mr_Wrong on 16/1/26.
 */
public class SelfieFragment extends BaseFragment {
    private SelfieApi mApi;
    private SelfieAdapter mAdapter;
    private int page = 191;
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

        setExitSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                names.clear();
                sharedElements.clear();

            }
        });

    }

    private void StartLoad(String page) {
        KLog.e("http://www.mzitu.com/share/comment-page-" + page);
        Utils.statrtRefresh(mRefresher, true);
        mSubscriptions.add(mApi.getSelfie(page)
                .map(new Func1<String, Elements>() {
                    @Override
                    public Elements call(String s) {
                        return Jsoup.parse(s).select("img[src~=(?i)\\.(png|jpe?g)]");
                    }
                })
                .flatMap(new Func1<Elements, Observable<String>>() {
                    @Override
                    public Observable<String> call(final Elements elements) {
                        return Observable.range(0, 19)
                                .map(new Func1<Integer, String>() {
                                    @Override
                                    public String call(Integer integer) {
                                        return elements.get(integer).toString().split("\"")[1];
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


    private Selfie handleSelfie(String url) {
        Selfie selfie = new Selfie();
        selfie.setUrl(url);
        try {
            Response response = new OkHttpClient().newCall(new Request.Builder().url(url).build()).execute();
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

        mAdapter = new SelfieAdapter(getContext()) {
            @Override
            protected void onItemClick(View v, int position) {
                startLargPicActivity(v, position);
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        AVQuery<AVObject> query = AVQuery.getQuery("SelfiePage");
        query.getFirstInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if (e == null) {
                    page = avObject.getNumber("page").intValue();
                    StartLoad("comment-page-" + page);
                } else {
                    KLog.e(e);
                }
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
        if (hasload) {
            return;
        }
        StartLoad("comment-page-" + --page);
        hasload = true;
    }

    @Override
    public void onRefresh() {
        mRefresher.setRefreshing(false);
    }
}
