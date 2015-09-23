package info.meizi.net;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import info.meizi.bean.Content;
import info.meizi.bean.Image;
import info.meizi.bean.TestContent;

/**
 * Created by Mr_Wrong on 15/9/14.
 * 解析具体大图页面
 */
public class ContentParser {
    public static Content Parser(String html) {
        Content content = new Content();
        Image image = new Image();
        Document doc = Jsoup.parse(html);
        Elements links = doc.select("img[src~=(?i)\\.(png|jpe?g)]");
        Element element = links.get(1).getElementsByTag("img").first();

        image.setUrl(element.attr("src"));
        content.setImage(image);
        // content.setUrl(element.attr("src"));
        content.setTitle(element.attr("alt"));
        return content;
    }

    public static TestContent ParserTestContent(String html) {
        TestContent content = new TestContent();
        Document doc = Jsoup.parse(html);
        Elements links = doc.select("img[src~=(?i)\\.(png|jpe?g)]");
        Element element = links.get(0).getElementsByTag("img").first();

        content.setUrl(element.attr("src"));
        content.setTitle(element.attr("alt"));




        return content;
    }



    public static int getCount(String html) {
        Document doc = Jsoup.parse(html);
        Elements pages = doc.select("span");
        Element page = pages.get(11);

        Pattern p = Pattern.compile("[\\d*]");
        Matcher m = p.matcher(page.toString());
        StringBuilder sb = new StringBuilder();
        StringBuffer stringBuffer  = new StringBuffer();
        while (m.find()) {
            sb.append(m.group());
            stringBuffer.append(m.group());
        }
        return Integer.parseInt(stringBuffer.toString());
    }
}
