package info.meizi_retrofit;

import java.text.DecimalFormat;

/**
 * Created by Mr_Wrong on 15/12/6.
 */
public class Alone {
    public static void main(String[] args) {
        String url = "http://pic.mmfile.net/2016/01/06r20.jpg";
        int i = 1;
        System.out.println(String.format("%s%s%s", url.substring(0, 33), new DecimalFormat("00").format(i), ".jpg"));



    }
}
