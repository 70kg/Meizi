package info.meizi_retrofit.model;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by Mr_Wrong on 15/12/1.
 */
public class Test extends RealmObject implements Serializable {
    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    String groupid;
}
