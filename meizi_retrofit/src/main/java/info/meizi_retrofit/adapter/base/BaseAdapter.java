package info.meizi_retrofit.adapter.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import info.meizi_retrofit.utils.PicassoHelper;

/**
 * Created by Mr_Wrong on 15/12/7.
 */
public abstract class BaseAdapter<E> extends ArrayRecyclerAdapter<E, BaseAdapter.ViewHolder> {
    protected Context mContext;
    protected Picasso mPicasso;

    protected BaseAdapter(Context context) {
        this.mContext = context;
        mPicasso = PicassoHelper.getInstance(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(mContext).inflate(getLayout(), parent, false);
        return new ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(BaseAdapter.ViewHolder viewHolder, final int position) {
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(v, position);
            }
        });
        onBind(viewHolder.getViewHolder(), position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private MyViewHolder viewHolder;

        public ViewHolder(View itemView) {
            super(itemView);
            viewHolder = MyViewHolder.getViewHolder(itemView);
        }

        public MyViewHolder getViewHolder() {
            return viewHolder;
        }
    }

    protected abstract void onItemClick(View v, int position);

    public abstract void onBind(MyViewHolder viewHolder, int Position);

    protected abstract int getLayout();

}
