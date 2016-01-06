package info.meizi_retrofit.model;

import java.io.Serializable;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.Sort;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Mr_Wrong on 15/12/1.
 * group的包装 主要用于收藏
 */
public class WrapGroup extends RealmObject implements Serializable {
    @PrimaryKey
    private String groupid;
    private Group group;
    private long date;
    private boolean iscollected;

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }


    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }


    public boolean iscollected() {
        return iscollected;
    }

    public void setIscollected(boolean iscollected) {
        this.iscollected = iscollected;
    }


    public static List<WrapGroup> all(Realm realm) {
        return realm.where(WrapGroup.class)
                .equalTo("iscollected", true)
                .findAllSorted("date", Sort.DESCENDING);
    }
}
