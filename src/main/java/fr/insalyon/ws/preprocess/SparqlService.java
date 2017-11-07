package fr.insalyon.ws.preprocess;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.util.Iterator;
import java.util.Set;

public class SparqlService {

    private final static String SPARQL_URL_FR = "http://fr.dbpedia.org/sparql";
    private final static String SPARQL_URL_EN = "http://dbpedia.org/sparql";

    public String getQueryFromKeys(Set<String> keywords){
        String filter = conditionBuilder(keywords);
        return "construct{?s ?p ?o} WHERE { FILTER((?o IN("+filter+"))&&(?s IN("+filter+")))" +" ?s ?p ?o. }";
    }

    public String getResultFromKeywords(Set<String> keywords,boolean useEn) throws UnirestException {
        return Unirest.post(useEn ? SPARQL_URL_EN:SPARQL_URL_FR)
                    .queryString("query", getQueryFromKeys(keywords))
                    .asString().getBody();
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
