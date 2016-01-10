package info.meizi_retrofit.adapter.base;

import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Mr_Wrong on 15/11/23.
 * 这个不是真正的viewholder 其实就是个容器。。 用起来方便点
 */
public class MyViewHolder {
    private SparseArray<View> viewHolder;
    private View view;


    public static MyViewHolder getViewHolder(View view) {
        MyViewHolder viewHolder = (MyViewHolder) view.getTag();
        if (viewHolder == null) {
            viewHolder = new MyViewHolder(view);
            view.setTag(viewHolder);
        }
        return viewHolder;
    }

    private MyViewHolder(View view) {
        this.view = view;
        viewHolder = new SparseArray<View>();
        view.setTag(viewHolder);
    }

    public <T extends View> T get(int id) {
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }

    public View getConvertView() {
        return view;
    }

    public TextView getTextView(int id) {

        return get(id);
    }

    public Button getButton(int id) {

        return get(id);
    }


    public ImageView getImageView(int id) {
        return get(id);
    }

    public void setTextView(int id, CharSequence charSequence) {
        getTextView(id).setText(charSequence);
    }


}
