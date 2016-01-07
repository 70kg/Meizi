package info.meizi_retrofit;

import java.text.DecimalFormat;
import java.text.Format;

/**
 * Created by Mr_Wrong on 15/12/6.
 */
public class Alone {
    public static void main(String[] args) {
        String url = "http://pic.mmfile.net/2016/01/06r20.jpg";
        int i = 91;
        Format f2 = new DecimalFormat("00");
        System.out.println(url.substring(0, 33) + f2.format(i) + ".jpg");
//        System.out.println("http://pic.mmfile.net/2016/01/06r".length());


    }

}
