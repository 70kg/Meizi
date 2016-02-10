package info.meizi_retrofit.adapter;

import android.content.Context;
import android.support.v4.view.ViewCompat;

import info.meizi_retrofit.R;
import info.meizi_retrofit.adapter.base.BaseAdapter;
import info.meizi_retrofit.adapter.base.MyViewHolder;
import info.meizi_retrofit.model.Group;
import info.meizi_retrofit.widget.RadioImageView;

/**
 * Created by Mr_Wrong on 15/10/30.
 */
public abstract class HomeAdapter extends BaseAdapter<Group> {
    protected HomeAdapter(Context context) {
        super(context);
    }

    @Override
    public void onBind(MyViewHolder viewHolder, int position) {
        Group bean = get(position);
        RadioImageView imageView = viewHolder.get(R.id.iv_main_item);
        imageView.setOriginalSize(bean.getWidth(), bean.getHeight());
        mPicasso.load(bean.getImageurl())
                .tag("1")
                .into(imageView);
        viewHolder.setTextView(R.id.text_title, bean.getTitle());
        ViewCompat.setTransitionName(imageView, bean.getUrl());
    }

    @Override
    public long getItemId(int position) {
        return get(position).getUrl().hashCode();
    }

    @Override
    protected int getLayout() {
        return R.layout.main_item;
    }
}
