package info.meizi_retrofit.adapter;

import android.content.Context;
import android.support.v4.view.ViewCompat;

import info.meizi_retrofit.R;
import info.meizi_retrofit.adapter.base.BaseAdapter;
import info.meizi_retrofit.adapter.base.MyViewHolder;
import info.meizi_retrofit.model.WrapGroup;
import info.meizi_retrofit.widget.RadioImageView;

/**
 * Created by Mr_Wrong on 15/12/1.
 */
public abstract class CollectedAdapter extends BaseAdapter<WrapGroup> {

    protected CollectedAdapter(Context context) {
        super(context);
    }

    @Override
    public void onBind(MyViewHolder viewHolder, int position) {
        WrapGroup bean = get(position);
        RadioImageView imageView = viewHolder.get(R.id.iv_main_item);
        imageView.setOriginalSize(bean.getGroup().getWidth(), bean.getGroup().getHeight());
        mPicasso.load(bean.getGroup().getImageurl()).tag("1").
                into(imageView);
        viewHolder.setTextView(R.id.text_title, bean.getGroup().getTitle());
        ViewCompat.setTransitionName(imageView, bean.getGroup().getUrl());
    }
    @Override
    protected int getLayout() {
        return R.layout.main_item;
    }
}

