package info.meizi_retrofit.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.LayoutRes;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Transformation;

import java.util.concurrent.CopyOnWriteArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import info.meizi_retrofit.R;
import info.meizi_retrofit.adapter.base.ArrayRecyclerAdapter;
import info.meizi_retrofit.model.Content;
import info.meizi_retrofit.utils.PicassoHelper;
import info.meizi_retrofit.widget.RadioImageView;

/**
 * Created by Mr_Wrong on 15/10/31.
 */
public abstract class GroupAdapter extends ArrayRecyclerAdapter<Content, GroupAdapter.ViewHolder> {
    private final Context context;
    private final LayoutInflater inflater;

    public GroupAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        setHasStableIds(true);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(R.layout.meizi_item, parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Content image = get(position);
        holder.imageView.setOriginalSize(image.getImagewidth(), image.getImageheight());

        PicassoHelper.getInstance(context).load(image.getUrl()).tag("1").config(Bitmap.Config.RGB_565).
                transform(new CopyOnWriteArrayList<Transformation>()).
                into(holder.imageView);
        holder.imageView.setTag(image.getUrl());
        ViewCompat.setTransitionName(holder.imageView, image.getUrl());
    }

    @Override
    public long getItemId(int position) {
        return get(position).getUrl().hashCode();
    }

    protected abstract void onItemClick(View v, int position);

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.iv_item)
        public RadioImageView imageView;

        public ViewHolder(@LayoutRes int resource, ViewGroup parent) {
            super(inflater.inflate(resource, parent, false));
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(v, getAdapterPosition());
                }
            });
        }

    }

}

