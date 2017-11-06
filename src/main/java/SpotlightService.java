import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class SpotlightService {

    final String BASE_URL = "http://model.dbpedia-spotlight.org/fr/annotate";

    //services
    public String SpotRDFFromURL(String text,float confidence,int support){
        String buildedUrl = BuildUrl(text,confidence,support);
        try {
            Document doc = Jsoup.connect(buildedUrl).header("Accept","application/xhtml+xml").get();
            Element div = doc.getElementsByTag("div").first();
            return div.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String BuildUrl(String text,float confidence,int support){
        return BASE_URL+"?text="+text+"&confidence="+confidence+"&support="+support;
    }
}
