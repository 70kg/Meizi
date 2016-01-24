package info.meizi_retrofit.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.socks.library.KLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import info.meizi_retrofit.R;
import info.meizi_retrofit.adapter.CollectedAdapter;
import info.meizi_retrofit.model.Group;
import info.meizi_retrofit.model.WrapGroup;
import info.meizi_retrofit.ui.base.ListActivity;
import info.meizi_retrofit.ui.group.GroupActivity;
import info.meizi_retrofit.utils.Utils;
import info.meizi_retrofit.widget.RadioImageView;

/**
 * Created by Mr_Wrong on 16/1/6.
 * 加载策略:先去加载数据库的 --> 展示 --> 然后去请求网络 --> 存到数据库 --> 从数据库获取和展示的进行比对 -->  更新界面
 */
public class CollectedActivity extends ListActivity {
    private CollectedAdapter mAdapter;
    List<WrapGroup> groups;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("我的收藏");
        groups = WrapGroup.all(realm);
        mAdapter = new CollectedAdapter(this) {
            @Override
            protected void onItemClick(View v, int position) {
                startGroupActivity(v, position);
            }
        };
        mRecyclerView.setAdapter(mAdapter);


        if (groups.size() > 0) {//本地有数据才去上传
            mAdapter.replaceWith(groups);
            if (mHasUser) {//已经登录
                saveToCloud();
            } else {
                showSnackBar("可以登录进行云端同步啦");
            }
        } else if (mHasUser) {//本地没有  并且已经登录
            getFromCloud();
        } else {
            showSnackBar("可以登录进行云端同步啦");
        }

    }

    private void saveToCloud() {
        try {
            JSONArray array = new JSONArray();
            for (WrapGroup group : groups) {
                JSONObject object = new JSONObject();
                object.put("groupid", group.getGroupid());
                object.put("title", group.getGroup().getTitle());
                object.put("imageurl", group.getGroup().getImageurl());
                object.put("url", group.getGroup().getUrl());
                array.put(object);
            }
            AVUser user = AVUser.getCurrentUser();
            user.put("groups", array);
            user.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (e == null) {
                        showSnackBar("云端同步完成");
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            KLog.e(e);
        }
    }


    private void getFromCloud() {
        try {
            groups = new ArrayList<>();
            AVUser user = AVUser.getCurrentUser();
            JSONArray array = user.getJSONArray("groups");
            if (array != null) {
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    WrapGroup wrapGroup = new WrapGroup();
                    Group group = new Group();
                    group.setWidth(236);
                    group.setHeight(354);
                    group.setImageurl(object.getString("imageurl"));
                    group.setTitle(object.getString("title"));
                    group.setUrl(object.getString("url"));
                    wrapGroup.setGroup(group);
                    wrapGroup.setGroupid(object.getString("groupid"));
                    groups.add(wrapGroup);
                }
                saveDb(groups);
                mAdapter.replaceWith(groups);
                showSnackBar("云端同步成功");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            KLog.e(e);
        }

    }

    private void startGroupActivity(View view, int position) {
        RadioImageView imageView = (RadioImageView) view.findViewById(R.id.iv_main_item);
        Bitmap bitmap = null;
        BitmapDrawable bd = (BitmapDrawable) imageView.getDrawable();
        if (bd != null) {
            bitmap = bd.getBitmap();
        }

        Intent intent1 = new Intent(this, GroupActivity.class);
        if (bitmap != null && !bitmap.isRecycled()) {
            intent1.putExtra(GroupActivity.COLOR, Utils.getPaletteColor(bitmap));
        }

        intent1.putExtra("title", mAdapter.get(position).getGroup().getTitle());
        intent1.putExtra("url", mAdapter.get(position).getGroup().getImageurl());
        intent1.putExtra(GroupActivity.GROUPID, mAdapter.get(position).getGroupid());
        startActivity(intent1);
    }

    @Override
    public void onRefresh() {
        List<WrapGroup> groups = WrapGroup.all(realm);
        mAdapter.replaceWith(groups);
        mRefresher.setRefreshing(false);
    }

    private void showSnackBar(CharSequence charSequence) {
        Snackbar.make(mRecyclerView, charSequence, Snackbar.LENGTH_SHORT).show();
    }
}
