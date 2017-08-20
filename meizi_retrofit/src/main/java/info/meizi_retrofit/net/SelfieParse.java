package info.meizi_retrofit.net;

import android.graphics.BitmapFactory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import info.meizi_retrofit.model.Selfie;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Mr_Wrong on 16/1/26.
 */
public class SelfieParse {
    public static List<Selfie> getSelfieList(String html) {
        Document doc = Jsoup.parse(html);
        Elements links = doc.select("img[src~=(?i)\\.(png|jpe?g)]");
        List<Selfie> lists = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Selfie selfie = new Selfie();
            Element element = links.get(i);
            String url = element.toString().split("\"")[1];

            selfie.setUrl(url);
            try {
                Response response = null;
                response = new OkHttpClient().newCall(new Request.Builder().url(url).build()).execute();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(response.body().byteStream(), null, options);
                selfie.setWidth(options.outWidth);
                selfie.setHeight(options.outHeight);
            } catch (IOException e) {
                e.printStackTrace();
            }
            lists.add(selfie);
        }
        return lists;
    }



}
