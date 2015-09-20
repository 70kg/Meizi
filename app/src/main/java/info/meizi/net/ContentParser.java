package info.meizi.net;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import info.meizi.bean.Content;

/**
 * Created by Mr_Wrong on 15/9/14.
 * 解析具体大图页面
 */
public class ContentParser {
    public static Content Parser(final String html) throws InterruptedException {
        final Content content = new Content();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Document doc = Jsoup.parse(html);
                Elements links = doc.select("img[src~=(?i)\\.(png|jpe?g)]");
                Element element = links.get(1).getElementsByTag("img").first();
                content.setUrl(element.attr("src"));
                content.setTitle(element.attr("alt"));
            }
        });
        thread.start();
        thread.join();
        return content;
    }

    public static int getCount(final String html) throws InterruptedException {
        final String[] s = new String[1];
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Document doc = Jsoup.parse(html);
                Elements pages = doc.select("span");
                Element page = pages.get(3);
                Pattern p = Pattern.compile("[\\d*]");
                Matcher m = p.matcher(page.toString());
                StringBuilder sb = new StringBuilder();
                while (m.find()) {
                    sb.append(m.group());
                }
                s[0] = (String) sb.toString().subSequence(1, sb.toString().length());
            }
        });
        thread.start();
        thread.join();
        return Integer.parseInt(s[0]);
    }
}
