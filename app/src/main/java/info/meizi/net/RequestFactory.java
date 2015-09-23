package info.meizi.net;

import com.squareup.okhttp.Request;

/**
 * Created by Mr_Wrong on 15/9/22.
 */
public class RequestFactory {
    public static final String URL = "http://www.mzitu.com/";

    public static Request make(String path) {
        return new Request.Builder().url(URL + path).build();
    }

}
