import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Set;

public class SparqlService {
    private final static String SPARQL_URL = "http://fr.dbpedia.org/sparql";


    public String getQueryFromKeys(Set<String> keywords){
        String filter = conditionBuilder(keywords);
        return "construct{?s ?p ?o} WHERE { FILTER((?o IN("+filter+"))&&(?s IN("+filter+")))" +" ?s ?p ?o. }";
    }

    public String getResultFromKeywords(Set<String> keywords) throws IOException {
        Document doc;
        String res = null;
        try {
            res = Unirest.post(SPARQL_URL)
                    .queryString("query", getQueryFromKeys(keywords))
                    .asString().getBody();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        doc = Jsoup.connect(SPARQL_URL)
                .userAgent("Mozilla/5.0")
                .data("query", getQueryFromKeys(keywords))
                .data("default-graph-uri", "http://fr.dbpedia.org")
                .data("timeout", "30000")
                .data("format", "auto")
                .get();
        return res;
    }

    private String conditionBuilder(Set<String> keywords){
        StringBuilder filterWords= new StringBuilder();
        Iterator<String> it = keywords.iterator();
        filterWords.append("<").append(it.next()).append(">");
        while (it.hasNext()) {
            filterWords.append(",<").append(it.next()).append(">");
        }
        return filterWords.toString();
    }
}
