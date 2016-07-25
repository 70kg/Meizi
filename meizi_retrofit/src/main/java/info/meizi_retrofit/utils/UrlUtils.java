package info.meizi_retrofit.utils;

import java.text.DecimalFormat;

/**
 * Created by Mr_Wrong on 16/1/7.
 */
public class UrlUtils {
    public static String handleUrl(String url, int index) {
//        http://i.meizitu.net/2016/07/24b26.jpg
        int i1 = url.lastIndexOf(".");
        String indexs = url.substring(i1 - 2, i1);
        return String.format("%s%s%s", url.substring(0, i1 - 2),
                new DecimalFormat("00").format(Integer.valueOf(indexs) + index), ".jpg");
    }
}
