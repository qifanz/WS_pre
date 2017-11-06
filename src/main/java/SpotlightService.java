import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class SpotlightService {

    final String BASE_URL = "http://model.dbpedia-spotlight.org/fr/annotate";

    //services
    public String SpotRDFFromURL(String text,float confidence,int support){
        String builtUrl="";
        try {
            builtUrl = BuildUrl(text,confidence,support);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        try {
            Document doc = Jsoup.connect(builtUrl).header("Accept","application/xhtml+xml").get();
            Element div = doc.getElementsByTag("div").first();
            return div.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private String BuildUrl(String text,float confidence,int support) throws UnsupportedEncodingException {
        return BASE_URL+"?text="+URLEncoder.encode(text, "UTF-8")+"&confidence="+confidence+"&support="+support;
    }
}
