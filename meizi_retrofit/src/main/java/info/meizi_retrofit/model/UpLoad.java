package info.meizi_retrofit.model;

import java.io.Serializable;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.Sort;
import io.realm.annotations.PrimaryKey;

/**
 * Created by wangpeng on 16/7/25.
 */
public class UpLoad extends RealmObject implements Serializable {
    @PrimaryKey
    private int versionCode;

    public static int getVerson(Realm realm) {
        List<UpLoad> upLoads = realm.where(UpLoad.class).findAllSorted("versionCode", Sort.DESCENDING);
        if (upLoads != null && upLoads.size() > 0) {
            return upLoads.get(0).getVersionCode();
        } else {
            return 0;
        }
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }
}
