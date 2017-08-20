package info.meizi_retrofit.ui.group;

import android.annotation.TargetApi;
import android.app.SharedElementCallback;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.socks.library.KLog;

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
import info.meizi_retrofit.net.Api;
import info.meizi_retrofit.net.ContentApi;
import info.meizi_retrofit.net.ContentParser;
import info.meizi_retrofit.ui.SaveAllService;
import info.meizi_retrofit.ui.base.ListActivity;
import info.meizi_retrofit.ui.largepic.LargePicActivity;
import info.meizi_retrofit.utils.UrlUtils;
import info.meizi_retrofit.utils.Utils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Mr_Wrong on 15/10/31.
 */
public class GroupActivity extends ListActivity {
    public static final String INDEX = "index";
    public static final String GROUPID = "groupid";
    public static final String URLS = "urls";
    private String groupid;
    private GroupAdapter mAdapter;
    private ContentApi mApi;
    private final OkHttpClient client = new OkHttpClient();
    private Bundle reenterState;
    private boolean iscollected;
    private WrapGroup mWrapGroup;
    int mI = 0;
    private String mUrl;
    private String mTitle;
    boolean canRefresh = true;
    int mIndex;
    int mCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//并没有卵用  还是从右边退出..搞不清楚
            getWindow().setReturnTransition(new Slide(Gravity.TOP));
        }

        mApi = createApi();
        groupid = getIntent().getStringExtra(GROUPID);
        mUrl = getIntent().getStringExtra("url");
        mTitle = getIntent().getStringExtra("title");
        if (mTitle != null) {
            setTitle(mTitle);
        }
        mAdapter = new GroupAdapter(this) {
            @Override
            protected void onItemClick(View v, int position) {
                startLargePicActivity(v, position);
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        sendToLoad(false);
        if (Build.VERSION.SDK_INT >= 22) {
            //这个在退出的时候调用  把当前的共享元素传递到后面的activity
            setExitSharedElementCallback(new SharedElementCallback() {
                @Override
                public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                    KLog.e("退出的");
                    if (reenterState != null) {
                        int i = reenterState.getInt(INDEX, 0);
                        sharedElements.clear();
                        sharedElements.put(mAdapter.get(i).getUrl(), mLayoutManager.findViewByPosition(i));
                        reenterState = null;
                    }
                }
            });
        }

        Group mGroup = realm.where(Group.class).equalTo("groupid", Integer.parseInt(groupid)).findFirst();

        mWrapGroup = new WrapGroup();
        mWrapGroup.setGroup(mGroup);

    }


    /**
     * 是否收藏了
     *
     * @param id
     * @param list
     * @return
     */
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
        KLog.e(groupid + "----" + isrefresh);
        Utils.statrtRefresh(mRefresher, true);
        if (isrefresh) {//刷新 从新加载
            newLoadData();
        } else if (!Content.all(realm, groupid).isEmpty()) {//数据库有 直接加载
            List<Content> list = Content.all(realm, groupid);
            mAdapter.replaceWith(list);
            Utils.statrtRefresh(mRefresher, false);
            KLog.d("获取本地资源");
        } else {//第一次加载
            KLog.d("加载网络资源");
            newLoadData();
        }
    }


    private void newLoadData() {
        mAdapter.clear();
        mIndex = 1;
        mSubscriptions.add(mApi.getContentCount(groupid)
                .flatMap(new Func1<String, Observable<Integer>>() {//获取到数量
                    @Override
                    public Observable<Integer> call(String s) {
                        return Observable.just(ContentParser.getCount(s));
                    }
                }).flatMap(new Func1<Integer, Observable<Integer>>() {
                    @Override
                    public Observable<Integer> call(Integer integer) {//23张
                        mCount = integer;
                        return Observable.range(1, integer);
                    }
                }).map(new Func1<Integer, Content>() {
                    @Override
                    public Content call(Integer integer) {
                        return newHandleContent(integer);
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Action1<Content>() {
                    @Override
                    public void call(Content content) {
                        saveDb(content);
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
            KLog.e(e);
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
        supportPostponeEnterTransition();
        reenterState = new Bundle(data.getExtras());
        mRecyclerView.scrollToPosition(reenterState.getInt(INDEX, 0));
        mRecyclerView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mRecyclerView.getViewTreeObserver().removeOnPreDrawListener(this);
                mRecyclerView.requestLayout();
                supportStartPostponedEnterTransition();
                return true;
            }
        });
    }

    private ContentApi createApi() {
        return Api.getInsatcne().createContentApi();
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

        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(this, view, mAdapter.get(position).getUrl());
        ActivityCompat.startActivity(this, intent, options.toBundle());
    }

    @Override
    public void onBackPressed() {
        ActivityCompat.finishAfterTransition(this);
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
        KLog.e("进入时的收藏个数" + list.size() + "_" + isContain(groupid, list));
        if (isContain(groupid, list)) {
            iscollected = true;
        } else {
            iscollected = false;
        }
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
                mWrapGroup.setGroupid(groupid);
                mWrapGroup.setDate(new Date().getTime());
                mWrapGroup.setIscollected(!iscollected);
                saveDb(mWrapGroup);

                if (!iscollected) {
                    item.setIcon(R.drawable.collected);
                    Toast.makeText(GroupActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
                } else {
                    item.setIcon(R.drawable.collect);
                    Toast.makeText(GroupActivity.this, "取消收藏成功", Toast.LENGTH_SHORT).show();
                }
                iscollected = mWrapGroup.iscollected();
                break;
            case R.id.menu_saveall:
                ArrayList<String> urls = new ArrayList<>();
                for (Content content : mAdapter.getList()) {
                    urls.add(content.getUrl());
                }
                Intent intent = new Intent(this, SaveAllService.class);
                intent.putExtra(SaveAllService.TITLE, mTitle);
                intent.putExtra(SaveAllService.GROUPID, groupid);
                intent.putExtra(SaveAllService.URLS, urls);
                startService(intent);
                break;
        }
        return true;
    }
}
