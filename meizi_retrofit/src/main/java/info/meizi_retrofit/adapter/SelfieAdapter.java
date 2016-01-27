package info.meizi_retrofit.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.ViewCompat;

import com.squareup.picasso.Transformation;

import java.util.concurrent.CopyOnWriteArrayList;

import info.meizi_retrofit.R;
import info.meizi_retrofit.adapter.base.BaseAdapter;
import info.meizi_retrofit.adapter.base.MyViewHolder;
import info.meizi_retrofit.model.Selfie;
import info.meizi_retrofit.utils.PicassoHelper;
import info.meizi_retrofit.widget.RadioImageView;

/**
 * Created by Mr_Wrong on 16/1/27.
 */
public abstract class SelfieAdapter extends BaseAdapter<Selfie> {
    Context mContext;

    protected SelfieAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public void onBind(MyViewHolder viewHolder, int Position) {
        Selfie selfie = get(Position);
        RadioImageView imageView = viewHolder.get(R.id.iv_item);
        imageView.setOriginalSize(selfie.getWidth(), selfie.getHeight());
        PicassoHelper.getInstance(mContext).load(selfie.getUrl()).tag("1").config(Bitmap.Config.RGB_565).
                transform(new CopyOnWriteArrayList<Transformation>()).
                into(imageView);
        imageView.setTag(selfie.getUrl());
        ViewCompat.setTransitionName(imageView, selfie.getUrl());
    }

    @Override
    protected int getLayout() {
        return R.layout.meizi_item;
    }
}
