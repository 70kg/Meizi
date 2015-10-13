package info.meizi.ui.largepic;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import info.meizi.R;
import info.meizi.widget.TouchImageView;

/**
 * Created by Mr_Wrong on 15/10/6.
 */
public class LargePicFragment extends Fragment {
    @Bind(R.id.image)
    TouchImageView image;
    private String url;
    private boolean initialShown;
    private LargePicActivity activity;

    @OnClick(R.id.image)
    void toggleToolbar() {
        activity.toggleToolbar();
    }

    public static Fragment newFragment(String url, boolean initialShown) {
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putBoolean("initial_shown", initialShown);
        LargePicFragment fragment = new LargePicFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (LargePicActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = getArguments().getString("url");
        initialShown = getArguments().getBoolean("initial_shown", false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_viewer, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Picasso.with(activity)
                .load(url)
                .into(image);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    View getSharedElement() {
        return image;
    }
}
