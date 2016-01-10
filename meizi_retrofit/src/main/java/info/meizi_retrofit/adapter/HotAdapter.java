package info.meizi_retrofit.adapter;

import android.content.Context;

import info.meizi_retrofit.R;
import info.meizi_retrofit.adapter.base.BaseAdapter;
import info.meizi_retrofit.adapter.base.MyViewHolder;

/**
 * Created by Mr_Wrong on 16/1/10.
 */
public abstract class HotAdapter extends BaseAdapter<String> {

    protected HotAdapter(Context context) {
        super(context);
    }

    @Override
    public void onBind(MyViewHolder viewHolder, int Position) {
        viewHolder.setTextView(R.id.tv_tag, get(Position));
    }

    @Override
    protected int getLayout() {
        return R.layout.tag_item;
    }
}
