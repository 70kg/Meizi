package info.meizi.net;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import info.meizi.bean.Content;
import info.meizi.utils.LogUtils;

/**
 * Created by Mr_Wrong on 15/9/14.
 * 解析具体大图页面
 */
public class ContentParser {
    private int count;
    public static Content Parser(String html) {
        Document doc = Jsoup.parse(html);
        Elements links = doc.select("img[src~=(?i)\\.(png|jpe?g)]");
        Content content = new Content();
        Element element = links.get(1).getElementsByTag("img").first();


        Elements pages = doc.select("span");
        Element page = pages.get(3);

        Pattern p = Pattern.compile("\\/(.*?)\\é¡µ");
        Matcher m = p.matcher(page.toString());
        while(m.find()) {
            content.setCount(m.group(1));
        }
        content.setUrl(element.attr("src"));
        content.setTitle(element.attr("alt"));
        return content;
    }
}
