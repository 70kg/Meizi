package info.meizi.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import info.meizi.base.BaseFragment;
import info.meizi.bean.Content;
import info.meizi.net.ContentParser;
import info.meizi.utils.LogUtils;

/**
 * Created by Mr_Wrong on 15/9/17.
 */
public class HomeFragment extends BaseFragment {
    private String path = "http://www.mzitu.com/";
    String groupid  ;
    private int mCount;

    public HomeFragment(String groupid) {
        this.groupid = groupid;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loaddata();

    }

    private void loaddata() {
        requestcount();
    }

    private void request(int count) {
        for (int i = 1; i < count + 1; i++) {
            executeRequest(new StringRequest( path+groupid+ "/" + String.valueOf(i),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            Content content = null;
                            try {
                                content = ContentParser.Parser(s);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            mDatas.add(content);
                            mRecyclerView.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                }
            }));
        }
    }

    /**
     * 先请求一遍count
     */
    private void requestcount() {
        executeRequest(new StringRequest(path+groupid,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            mCount = ContentParser.getCount(s);
                            LogUtils.d("mcount---"+mCount);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        request(mCount);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        }));
    }
}
