package info.meizi_retrofit.net;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import info.meizi_retrofit.model.Content;
import info.meizi_retrofit.model.Group;

/**
 * Created by Mr_Wrong on 15/9/14.
 * 解析具体大图页面
 */
public class ContentParser {

    //获取首页的list
    public static List<Group> ParserGroups(String html, String type) {
        List<Group> list = new ArrayList<>();
        Document doc = Jsoup.parse(html);
        Elements links = doc.getElementById("pins").getElementsByTag("li");

        Element aelement;
        Element imgelement;
        for (int i = 0; i < 24; i += 1) {
            if (i < links.size() && links.get(i) != null && links.get(i).select("img").first() != null) {
                imgelement = links.get(i).select("img").first();
                aelement = links.get(i).select("a").first();
                Group bean = new Group();
                bean.setOrder(i);
                bean.setTitle(imgelement.attr("alt"));
                bean.setImageurl(imgelement.attr("data-original"));

                bean.setType(type);
                bean.setHeight(354);//element.attr("height")
                bean.setWidth(236);
                if (aelement != null) {
                    bean.setUrl(aelement.attr("href"));
                }
                bean.setGroupid(url2groupid(bean.getUrl()));//首页的这个是从大到小排序的 可以当做排序依据
                list.add(bean);
            } else {
                break;
            }
        }
        return list;
    }

    public static int getCount(String html) {
        Document doc = Jsoup.parse(html);
        Element page = doc.getElementsByClass("pagenavi").get(0).getElementsByTag("a").get(4).getElementsByTag("span").first();
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
