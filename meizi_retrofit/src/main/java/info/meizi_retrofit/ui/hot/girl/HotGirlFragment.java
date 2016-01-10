package info.meizi_retrofit.ui.hot.girl;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import info.meizi_retrofit.R;
import info.meizi_retrofit.adapter.HotAdapter;

/**
 * Created by Mr_Wrong on 16/1/10.
 */
public class HotGirlFragment extends Fragment {
    @Bind(R.id.rv_hot)
    RecyclerView mRecyclerView;
    private View rootView;
    private HotAdapter mAdapter;
    List<String> girls = new ArrayList<>();
    List<String> girlUrls = new ArrayList<>();

    public HotGirlFragment() {
    }

    public static HotGirlFragment newFragment() {
        Bundle bundle = new Bundle();
        HotGirlFragment fragment = new HotGirlFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.hot_tag, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        girls = Arrays.asList(getResources().getStringArray(R.array.girls));
        girlUrls = Arrays.asList(getResources().getStringArray(R.array.girlUrls));
        mAdapter = new HotAdapter(getContext()) {
            @Override
            protected void onItemClick(View v, int position) {
                Intent intent = new Intent(getActivity(), HotGirlGroupActivity.class);
                intent.putExtra(HotGirlGroupActivity.GIRL, girlUrls.get(position));
                intent.putExtra(HotGirlGroupActivity.TITLE, girls.get(position));
                startActivity(intent);
            }
        };
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.addAll(girls);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
