package info.meizi.net;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import info.meizi.bean.Content;
import info.meizi.bean.MainBean;

/**
 * Created by Mr_Wrong on 15/9/14.
 * 解析具体大图页面
 */
public class ContentParser {


    public static Content ParserContent(String html) {
        Content content = new Content();
        Document doc = Jsoup.parse(html);
        Elements links = doc.select("img[src~=(?i)\\.(png|jpe?g)]");
        Element element = links.get(0).getElementsByTag("img").first();
        content.setUrl(element.attr("src"));
        content.setTitle(element.attr("alt"));
        return content;
    }

    //获取首页的list
    public static List<MainBean> ParserMainBean(String html, String type) {
        List<MainBean> list = new ArrayList<>();
        Document doc = Jsoup.parse(html);
        Elements links = doc.select("li");//.select("a[target]");

        Element aelement;
        Element imgelement;
        for (int i = 7; i < links.size(); i++) {
            imgelement = links.get(i).select("img").first();
            aelement = links.get(i).select("a").first();
            MainBean bean = new MainBean();
            bean.setOrder(i);

            bean.setTitle(imgelement.attr("alt").toString());
            bean.setType(type);
            bean.setHeight(354);//element.attr("height")
            bean.setWidth(236);
            bean.setImageurl(imgelement.attr("data-original"));
            bean.setUrl(aelement.attr("href"));
            bean.setGroupid(url2groupid(bean.getUrl()));//首页的这个是从大到小排序的 可以当做排序依据
            list.add(bean);
        }
        return list;
    }

    public static int getCount(String html) {
        Document doc = Jsoup.parse(html);
        Elements pages = doc.select("span");
        Element page = pages.get(11);

        Pattern p = Pattern.compile("[\\d*]");
        Matcher m = p.matcher(page.toString());
        StringBuffer stringBuffer = new StringBuffer();
        while (m.find()) {
            stringBuffer.append(m.group());
        }
        return Integer.parseInt(stringBuffer.toString());
    }

    private static int url2groupid(String url) {
        return Integer.parseInt(url.split("/")[3]);
    }

}
