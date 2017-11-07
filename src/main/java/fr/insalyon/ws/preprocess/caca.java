package fr.insalyon.ws.preprocess;

import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.jena.rdf.model.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by qifan on 2017/11/6.
 */
public class caca {
    private final static String LANG_SEARCH = "lang_fr";
    private final static String SEARCH_URL = "https://www.google.com/search";
    private final static String SPARQL_URL = "http://fr.dbpedia.org/sparql";

    public static void main(String[] args) {

        PreprocessService ps = new PreprocessService(System.getProperty("user.dir"),false);
        try {
            List<String> a = ps.ProcessQuery("Barack Obama",3,0);
            int i = 0;
            for (String s:
                 a) {
                Set<String> set = ps.ProcessSpotlight(s,0.4f);
                int res = ps.ProcessSparql(set,Integer.toString(i++));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*PreprocessService ps = new PreprocessService("",false);
        try {
            ps.ProcessQuery("caa",5,0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SpotlightService spotlightService = new SpotlightService();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the search term.");
        String searchTerm = scanner.nextLine();
        System.out.println("Please enter the number of results. Example: 5 10 20");
        int num = scanner.nextInt();
        scanner.close();

        String searchURL = SEARCH_URL + "?q=" + searchTerm + "&num=" + 2 * num + "&lr=" + LANG_SEARCH;
        //without proper User-Agent, we will get 403 error
        Document doc = null;
        try {
            doc = Jsoup.connect(searchURL).userAgent("Mozilla/5.0").get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements results = null;
        if (doc != null) {
            results = doc.select("h3.r > a");
        }


        int countResults = 0;
        for (Element result : results) {
            if (countResults >= num) {
                break;
            }
            String linkHref = result.attr("href");
            String linkText = result.text();
            String url = linkHref.substring(7, linkHref.indexOf("&"));
            if ((url.contains("http://") || url.contains("https://"))) {
                System.out.println(url);
                Document caca = null;
                try {
                    caca = Jsoup.connect(url).userAgent("Mozilla/5.0").get();
                    String scaca = caca.body().select("p").text();
                    System.out.println(scaca);
                    System.out.println("-----------");
                    Set<String> keywords = spotlightService.SpotRDFFromURL(scaca, 0.5f, false);
                    SparqlService ss = new SparqlService();
                    String res = null;
                    try {
                        res = ss.getResultFromKeywords(keywords,false);
                    } catch (UnirestException e) {
                        e.printStackTrace();
                    }
                    PrintWriter out = new PrintWriter(countResults+".n3");
                    out.println(res);
                    out.close();
                    Model model = ModelFactory.createDefaultModel();
                    model.read(countResults+".n3","N3");
                    PrintWriter out2 = new PrintWriter(countResults+".rdf");
                    model.write(out2,"RDF/XML");
                    out2.close();
                    countResults++;
                } catch (IOException e) {
                    continue;
                }
            }

            //System.out.println("Text::" + linkText + ", URL::" + linkHref);//.substring(7, linkHref.indexOf("&")));
*/        }

    }

