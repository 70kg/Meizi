package info.meizi_retrofit.model;

import java.io.Serializable;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.Sort;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Mr_Wrong on 15/10/30.
 */
public class Group extends RealmObject implements Serializable {
    private int count;
    private int width;
    private int height;
    @PrimaryKey
    private String imageurl;
    private String url;
    private String title;
    private String type;
    private int order;
    private long date;
    private int color;
    private boolean iscollected;
    private int groupid;


    private static volatile Group instance = null;

    public static Group getInstance() {
        if (instance == null) {
            synchronized (Group.class) {
                if (instance == null) {
                    instance = new Group();
                }
            }
        }
        return instance;
    }


    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }


    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }


    public boolean getIscollected() {
        return iscollected;
    }

    public void setIscollected(boolean iscollected) {
        this.iscollected = iscollected;
    }


    public int getGroupid() {
        return groupid;
    }

    public void setGroupid(int groupid) {
        this.groupid = groupid;
    }

    public static List<Group> all(Realm realm, String type) {
        return realm.where(Group.class)
                .equalTo("type", type)
                .findAllSorted("groupid", Sort.DESCENDING);
    }

    public static List<Group> allCollected(Realm realm) {
        return realm.where(Group.class)
                .equalTo("iscollected", true)
                .findAllSorted("date", Sort.DESCENDING);
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
