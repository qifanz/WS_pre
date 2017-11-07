package fr.insalyon.ws.preprocess;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;

public class SpotlightService {

    private final String BASE_URL_FR = "http://model.dbpedia-spotlight.org/fr/annotate";
    private final String BASE_URL_EN = "http://model.dbpedia-spotlight.org/en/annotate";

    //services
    public Set<String> SpotRDFFromURL(String url,float confidence,boolean useEn) throws IOException {
        String builtUrl;
        Set<String> keywords=new HashSet<String>();
        Document html = Jsoup.connect(url).userAgent("Mozilla/5.0").get();
        String text = html.body().select("p").text();
        builtUrl = BuildUrl(text,confidence,useEn);
        Document doc = Jsoup.connect(builtUrl).header("Accept","application/xhtml+xml").get();
        Element div = doc.getElementsByTag("div").first();
        Elements results=div.select("a");
        for (Element result : results) {
            String linkHref = result.attr("href");
            keywords.add(linkHref);
        }
        return keywords;
    }
    private String BuildUrl(String text,float confidence,boolean useEn) throws UnsupportedEncodingException {

        if(text.length()>3000){
            text = text.substring(0,3000);
        }
        return (useEn ? BASE_URL_EN: BASE_URL_FR) +"?text="+URLEncoder.encode(text, "UTF-8")+"&confidence="+confidence;
    }
}
