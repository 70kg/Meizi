package info.meizi.bean;

import io.realm.RealmObject;

/**
 * Created by Mr_Wrong on 15/9/14.
 */
public class Content extends RealmObject {
    private String url;//图片地址
    private String title;//标题
    private String count;//每个group的数量
    private Image image;


    public static Content from(Image image) {
        return new Content(image);
    }

    public Content(){

    }
    private Content(Image image) {
        this.url = image.getUrl();
    }
    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

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
