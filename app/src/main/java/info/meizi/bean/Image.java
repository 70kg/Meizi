package info.meizi.bean;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by Mr_Wrong on 15/9/22.
 */
public class Image extends RealmObject {
    private int width;
    private int height;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String url;
    public static List<Image> all(Realm realm) {
        return realm.where(Image.class)
                .findAllSorted("url", RealmResults.SORT_ORDER_DESCENDING);
    }
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }



}
