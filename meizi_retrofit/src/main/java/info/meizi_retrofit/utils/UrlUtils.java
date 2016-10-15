package info.meizi_retrofit.utils;

import java.text.DecimalFormat;

/**
 * Created by Mr_Wrong on 16/1/7.
 */
public class UrlUtils {
    public static String handleUrl(String url, int index) {
//        http://i.meizitu.net/2016/07/24b26.jpg
        String[] paths = url.split("/");
        return "http://i.meizitu.net/"+paths[4]+"/"+paths[5]+"/"+paths[6].split("_")[1].substring(0, 3) + new DecimalFormat("00").format(index) + ".jpg";
    }
}
