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

    final String BASE_URL = "http://model.dbpedia-spotlight.org/fr/annotate";

    //services
    public Set<String> SpotRDFFromURL(String text,float confidence,int support){
        String builtUrl="";
        Set<String> keywords=new HashSet<String>();
        try {
            builtUrl = BuildUrl(text,confidence,support);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            Document doc = Jsoup.connect(builtUrl).header("Accept","application/xhtml+xml").get();
            Element div = doc.getElementsByTag("div").first();
            Elements results=div.select("a");
            for (Element result : results) {
                String linkHref = result.attr("href");
                keywords.add(linkHref);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return keywords;
    }
    private String BuildUrl(String text,float confidence,int support) throws UnsupportedEncodingException {

        if(text.length()>4500){
            text = text.substring(0,4500);
        }
        return BASE_URL+"?text="+URLEncoder.encode(text, "UTF-8")+"&confidence="+confidence+"&support="+support;
    }
}
