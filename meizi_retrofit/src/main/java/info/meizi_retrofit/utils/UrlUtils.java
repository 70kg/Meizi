package info.meizi_retrofit.utils;

import java.text.DecimalFormat;

/**
 * Created by Mr_Wrong on 16/1/7.
 */
public class UrlUtils {
    public static String handleUrl(String url, int index) {
        return url.substring(0, 33) + new DecimalFormat("00").format(index) + ".jpg";
    }
}
