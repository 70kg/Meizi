package info.meizi.bean;

/**
 * Created by Mr_Wrong on 15/9/14.
 */
public class Content {
    String url;//图片地址
    String title;//标题
    String count;//每个group的数量

    public String getCount() {
        return count;
    }
    public void setCount(String count) {
        this.count = count;
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
