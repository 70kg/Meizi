package info.meizi_retrofit.adapter;

import android.content.Context;
import android.graphics.Bitmap;

import info.meizi_retrofit.R;
import info.meizi_retrofit.adapter.base.BaseAdapter;
import info.meizi_retrofit.adapter.base.MyViewHolder;
import info.meizi_retrofit.model.Content;
import info.meizi_retrofit.widget.RadioImageView;

/**
 * Created by Mr_Wrong on 15/10/31.
 */
public abstract class GroupAdapter extends BaseAdapter<Content> {

    protected GroupAdapter(Context context) {
        super(context);
    }

    @Override
    public void onBind(MyViewHolder viewHolder, int position) {
        Content image = get(position);
        RadioImageView imageView = viewHolder.get(R.id.iv_item);
        imageView.setOriginalSize(image.getImagewidth(), image.getImageheight());
        mPicasso.load(image.getUrl()).tag("1").config(Bitmap.Config.RGB_565).
                into(imageView);
    }

    @Override
    protected int getLayout() {
        return R.layout.meizi_item;
    }

    @Override
    public long getItemId(int position) {
        return get(position).getUrl().hashCode();
    }
}

