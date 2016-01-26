package info.meizi_retrofit.net;

import com.socks.library.KLog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Created by Mr_Wrong on 16/1/26.
 */
public class SelfieParse {
    public static int getPage(String html) {
        Document doc = Jsoup.parse(html);
        Elements links = doc.select("img[src~=(?i)\\.(png|jpe?g)]");
        KLog.e(links);
        return 1;

    }
}
