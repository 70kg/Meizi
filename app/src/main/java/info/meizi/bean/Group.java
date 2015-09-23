package info.meizi.bean;

/**
 * Created by Mr_Wrong on 15/9/22.
 * 一个号码对应的是一个group
 * 一个group有多个content
 * 每个content有一个image
 */
public class Group {

    int groupid;
    int count;

    public int getGroupid() {
        return groupid;
    }

    public void setGroupid(int groupid) {
        this.groupid = groupid;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
