package info.meizi.utils;

import android.graphics.Bitmap;

import com.squareup.picasso.Transformation;

/**
 * Created by Mr_Wrong on 15/9/23.
 */
public class CropSquareTransformation  implements Transformation {
    @Override public Bitmap transform(Bitmap source) {
        int size = Math.min(source.getWidth(), source.getHeight());
        int x = (source.getWidth() - size) / 4;
        int y = (source.getHeight() - size) / 4;
        Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
        if (result != source) {
            source.recycle();
        }
        return result;
    }

    @Override public String key() { return "square()"; }
}
