package info.meizi_retrofit.ui;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.socks.library.KLog;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import info.meizi_retrofit.R;
import info.meizi_retrofit.adapter.GroupAdapter;
import info.meizi_retrofit.model.Content;
import info.meizi_retrofit.model.Group;
import info.meizi_retrofit.model.WrapGroup;
import info.meizi_retrofit.net.ContentApi;
import info.meizi_retrofit.net.ContentParser;
import info.meizi_retrofit.ui.base.ListActivity;
import info.meizi_retrofit.utils.LogUtils;
import info.meizi_retrofit.utils.StringConverter;
import info.meizi_retrofit.utils.UrlUtils;
import info.meizi_retrofit.utils.Utils;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by Mr_Wrong on 15/10/31.
 */
public class GroupActivity extends ListActivity implements SwipeRefreshLayout.OnRefreshListener {
    public static final String INDEX = "index";
    public static final String GROUPID = "groupid";
    public static final String COLOR = "color";
    public static final String URLS = "urls";
    private String groupid;
    public int color;
    private GroupAdapter mAdapter;
    private ContentApi mApi;
    private final OkHttpClient client = new OkHttpClient();
    private Bundle reenterState;
    private boolean iscollected;
    private WrapGroup mWrapGroup;
    int mI = 0;
    String mUrl;
    boolean canRefresh = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mApi = createApi();
        groupid = getIntent().getStringExtra(GROUPID);
        color = getIntent().getIntExtra(COLOR, getResources().getColor(R.color.app_primary_color));
        mUrl = getIntent().getStringExtra("url");

        Utils.setSystemBar(this, mToolbar, color);

        mRefresher.setColorSchemeColors(color);

        mAdapter = new GroupAdapter(this) {
            @Override
            protected void onItemClick(View v, int position) {
                startLargePicActivity(v, position);
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        sendToLoad(false);

        if (Build.VERSION.SDK_INT >= 22) {
            setExitSharedElementCallback(new SharedElementCallback() {
                @Override
                public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                    if (reenterState != null) {
                        int i = reenterState.getInt(INDEX, 0);
                        sharedElements.clear();
                        sharedElements.put(mAdapter.get(i).getUrl(), mLayoutManager.findViewByPosition(i));
                        reenterState = null;
                    }
                }
            });
        }

    }


    private boolean isContain(String id, List<WrapGroup> list) {
        boolean b = false;
        for (WrapGroup test : list) {
            if (test.getGroupid().endsWith(id)) {
                b = true;
                break;
            }
        }
        return b;
    }


    private void sendToLoad(boolean isrefresh) {
        LogUtils.e(groupid + "----" + isrefresh);
        Utils.statrtRefresh(mRefresher, true);
        if (isrefresh) {
            newLoadData();
        } else if (!Content.all(realm, groupid).isEmpty()) {//数据库有 直接加载
            List<Content> list = Content.all(realm, groupid);
            mAdapter.replaceWith(list);
            Utils.statrtRefresh(mRefresher, false);
            LogUtils.d("获取本地资源");
        } else {
            LogUtils.d("加载网络资源");
            newLoadData();
        }
    }


    private void newLoadData() {
        mAdapter.clear();
        mSubscriptions.add(mApi.getContentCount(groupid)
                .flatMap(new Func1<String, Observable<Integer>>() {//获取到数量
                    @Override
                    public Observable<Integer> call(String s) {
                        return Observable.just(ContentParser.getCount(s));
                    }
                }).flatMap(new Func1<Integer, Observable<Integer>>() {
                    @Override
                    public Observable<Integer> call(Integer integer) {//23张
                        return Observable.range(1, integer);
                    }
                }).map(new Func1<Integer, Content>() {
                    @Override
                    public Content call(Integer integer) {
                        return newHandleContent(integer);
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Action1<Content>() {
                    @Override
                    public void call(Content content) {
                        saveDB(content);
                    }
                })
                .subscribe(new Subscriber<Content>() {
                    @Override
                    public void onCompleted() {
                        canRefresh = false;
                        mRefresher.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e(e);
                        Snackbar.make(mRecyclerView, "出现错误啦", Snackbar.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Content content) {
                        mAdapter.add(content);
                    }
                }));
    }

    private Content newHandleContent(int index) {
        String url = UrlUtils.handleUrl(mUrl, index);
        Content content = new Content();
        content.setUrl(url);
        try {
            handleContent(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    private Content handleContent(Content content) throws IOException {
        Response response = client.newCall(new Request.Builder().url(content.getUrl()).build()).execute();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(response.body().byteStream(), null, options);
        content.setImagewidth(options.outWidth);
        content.setImageheight(options.outHeight);
        content.setGroupid(groupid);
        content.setOrder(Integer.parseInt(groupid + mI++));
        return content;
    }

    @TargetApi(22)
    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        supportStartPostponedEnterTransition();
        reenterState = new Bundle(data.getExtras());
        mRecyclerView.scrollToPosition(reenterState.getInt(INDEX, 0));
        mRecyclerView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mRecyclerView.getViewTreeObserver().removeOnPreDrawListener(this);
                mRecyclerView.requestLayout();
                return true;
            }
        });
    }

    private ContentApi createApi() {
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(ContentApi.BASE_URL)
                .setConverter(new StringConverter())
                .setClient(new OkClient())
                .build();
        return adapter.create(ContentApi.class);
    }

    private void startLargePicActivity(View view, int position) {
        ArrayList<String> urls = new ArrayList<>();
        for (Content content : mAdapter.getList()) {
            urls.add(content.getUrl());
        }

        Intent intent = new Intent(this, LargePicActivity.class);
        intent.putExtra(INDEX, position);
        intent.putExtra(GROUPID, groupid);
        intent.putExtra(URLS, urls);

        if (Build.VERSION.SDK_INT >= 22) {
            ActivityOptionsCompat options = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(this, view, mAdapter.get(position).getUrl());
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }

    }

    @Override
    public void onRefresh() {
        if (canRefresh) {
            sendToLoad(true);
        } else {
            mRefresher.setRefreshing(false);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //判断有没有收藏过
        List<WrapGroup> list = WrapGroup.all(realm);
        LogUtils.e("进入时的收藏个数" + list.size() + "_" + isContain(groupid, list));
        if (isContain(groupid, list)) {
            iscollected = true;
        } else {
            iscollected = false;
        }
        Group mGroup = realm.where(Group.class).equalTo("groupid", Integer.parseInt(groupid)).findFirst();
        mWrapGroup = new WrapGroup();
        mWrapGroup.setGroup(mGroup);
        getMenuInflater().inflate(R.menu.group_menu, menu);
        if (iscollected) {
            menu.findItem(R.id.menu_collect).setIcon(R.drawable.collected);
        } else {
            menu.findItem(R.id.menu_collect).setIcon(R.drawable.collect);
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_collect:
                realm.beginTransaction();
                mWrapGroup.setGroupid(groupid);
                mWrapGroup.setDate(new Date().getTime());
                mWrapGroup.setIscollected(!iscollected);
                realm.copyToRealmOrUpdate(mWrapGroup);
                realm.commitTransaction();


                LogUtils.e("点击收藏后的个数:" + WrapGroup.all(realm).size());

                if (!iscollected) {
                    item.setIcon(R.drawable.collected);
                    Toast.makeText(GroupActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
                } else {
                    item.setIcon(R.drawable.collect);
                    Toast.makeText(GroupActivity.this, "取消收藏成功", Toast.LENGTH_SHORT).show();
                }
                iscollected = mWrapGroup.iscollected();

                break;
        }
        return true;
    }
}
