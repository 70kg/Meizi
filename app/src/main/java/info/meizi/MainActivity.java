package info.meizi;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import info.meizi.adapter.MeiziAdapter;
import info.meizi.base.BaseActivity;
import info.meizi.bean.Content;
import info.meizi.net.ContentParser;

public class MainActivity extends BaseActivity {
    private static final String URL = "http://www.mzitu.com/";
    private static final String GROUP_ID = "38259";
    @Bind(R.id.id_recyclerview)
    RecyclerView mRecyclerView;

    private String path = URL + GROUP_ID;

    private int mCount = 1;//请求次数

    private List<Content> mDatas = new ArrayList<Content>();
    private MeiziAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        loaddata();
        initviews();
    }

    private void initviews() {
        mAdapter = new MeiziAdapter(this, mDatas);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void loaddata() {
        request(10, 0);
    }

    private void request(int count, int from) {
        for (int i = 0; i < count; i++) {
            executeRequest(new StringRequest(path + "/" + String.valueOf(++from),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            boolean hasrequest = false;
                            Content content = ContentParser.Parser(s);
                            mCount = Integer.parseInt(content.getCount());
                            if (mCount > 10 && hasrequest == false) {
                                hasrequest = true;
                                request(mCount - 10, 10);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
