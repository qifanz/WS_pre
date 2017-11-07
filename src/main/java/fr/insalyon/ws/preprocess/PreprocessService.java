package fr.insalyon.ws.preprocess;

import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.FileNotFoundException;
import java.io.FilePermission;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.AccessControlException;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class PreprocessService {

    private final static String LANG_FR = "lang_fr";
    private final static String LANG_EN = "lang_en";
    private final static String SEARCH_URL = "https://www.google.com/search";

    public String filePath;
    public boolean useEn;

    public PreprocessService(String path, boolean useEnglish){
        filePath = path;
        useEn = useEnglish;
    }

    public  List<String> ProcessQuery(String query, int number,int offset) throws IOException {
        ArrayList<String> arr = new ArrayList<String>();
        String searchURL = SEARCH_URL + "?q=" + query + "&num=" + 2 * number + "&lr=" + (useEn ? LANG_EN : LANG_FR);
        if(offset > 0) searchURL = searchURL.concat("&offset="+offset);
        Document doc = Jsoup.connect(searchURL).userAgent("Mozilla/5.0").get();
        Elements results = null;
        if (doc != null) {
            results = doc.select("h3.r > a");
        }
        int countResults = 0;
        if (results != null) {
            for (Element result : results) {
                if (countResults >= number) break;
                String linkHref = result.attr("href");
                String url = linkHref.substring(7, linkHref.indexOf("&"));
                if ((url.contains("http://") || url.contains("https://")) && !url.contains("twitter.com")) {
                    arr.add(url);
                }
            }
        }
        return arr;
    }

    public Set<String> ProcessSpotlight(String text,float confidence) throws IOException {
        SpotlightService ss = new SpotlightService();
        return ss.SpotRDFFromURL(text,confidence,useEn);
    }

    public int ProcessSparql(Set<String> keywords,String filename) throws UnirestException, FileNotFoundException {
        SparqlService ss = new SparqlService();
        String res = ss.getResultFromKeywords(keywords,useEn);
//        try {
//            AccessController.checkPermission(new FilePermission(filePath, "read,write"));
//        } catch (AccessControlException e) {
//            return 1;
//        }
        PrintWriter out = new PrintWriter(filePath+"/"+filename+".n3");
        out.println(res);
        out.close();
        Model model = ModelFactory.createDefaultModel();
        model.read(filename+".n3","N3");
        PrintWriter out2 = new PrintWriter(filePath+"/"+filename+".rdf");
        model.write(out2,"RDF/XML");
        out2.close();
        return 0;
    }

    public void changeLangTo(boolean useEnglish){
        useEn = useEnglish;
    }

}
