package info.meizi_retrofit.ui.largepic;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import java.io.File;

import info.meizi_retrofit.R;
import info.meizi_retrofit.utils.PicassoHelper;
import info.meizi_retrofit.utils.RxMeizhi;
import info.meizi_retrofit.widget.TouchImageView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Mr_Wrong on 15/10/6.
 */
public class LargePicFragment extends Fragment {
    private static final String URL = "url";
    private static final String GROUPID = "groupid";
    private static final String POSITION = "position";
    private String url;
    private LargePicActivity activity;
    private String groupid;
    private int position;
    private TouchImageView view;
    protected CompositeSubscription mSubscriptions = new CompositeSubscription();

    public static Fragment newFragment(String url, String groupid, int position) {
        Bundle bundle = new Bundle();
        bundle.putString(URL, url);
        bundle.putString(GROUPID, groupid);
        bundle.putInt(POSITION, position);
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
        url = getArguments().getString(URL);
        groupid = getArguments().getString(GROUPID);
        position = getArguments().getInt(POSITION);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = (TouchImageView) inflater.inflate(R.layout.fragment_viewer, container, false);


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.supportFinishAfterTransition();
            }
        });

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setMessage("保存图片");
                builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mSubscriptions.add(
                                RxMeizhi.saveImageAndGetPathObservable(getContext(), url, groupid + "_" + position)
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Action1<Uri>() {
                                            @Override
                                            public void call(Uri uri) {
                                                File appDir = new File(Environment.getExternalStorageDirectory(), "Meizi");
                                                String msg = String.format("图片已保存至 %s 文件夹", appDir.getAbsolutePath());
                                                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                                            }
                                        }));
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
                return true;
            }
        });
        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                view.getViewTreeObserver().removeOnPreDrawListener(this);
                getActivity().supportStartPostponedEnterTransition();
                return true;
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        PicassoHelper.getInstance(activity)
                .load(url)
                .into(view);
    }

    public View getSharedElement() {
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.mSubscriptions != null) {
            this.mSubscriptions.unsubscribe();
        }
    }
}
