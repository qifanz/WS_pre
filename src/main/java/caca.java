import org.apache.jena.rdf.model.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by qifan on 2017/11/6.
 */
public class caca {
    private final static String LANG_SEARCH = "lang_fr";
    private final static String SEARCH_URL = "https://www.google.com/search";
    private final static String SPARQL_URL = "http://fr.dbpedia.org/sparql";


    public static void main(String[] args) {
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

        //below will print HTML data, save it to a file and open in browser to compare
        //System.out.println(doc.html());

        //If google search results HTML change the <h3 class="r" to <h3 class="r1"
        //we need to change below accordingly
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
                    Set<String> keywords = spotlightService.SpotRDFFromURL(scaca, 0.5f, 20);
                    SparqlService ss = new SparqlService();
                    String res = ss.getResultFromKeywords(keywords);
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
        }

    }

}

