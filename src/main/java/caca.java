import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by qifan on 2017/11/6.
 */
public class caca {
    public final static String LANG_SEARCH = "lang_fr";
    public final static String SEARCH_URL = "https://www.google.com/search";



    public static void main(String[] args) {
        SpotlightService spotlightService=new SpotlightService();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the search term.");
        String searchTerm = scanner.nextLine();
        System.out.println("Please enter the number of results. Example: 5 10 20");
        int num = scanner.nextInt();
        scanner.close();

        String searchURL = SEARCH_URL + "?q=" + searchTerm + "&num=" + num + "&lr=" + LANG_SEARCH;
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
        Elements results = doc.select("h3.r > a");

        for (Element result : results) {
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
                    System.out.println(spotlightService.SpotRDFFromURL(scaca,0.5f,20));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //System.out.println("Text::" + linkText + ", URL::" + linkHref);//.substring(7, linkHref.indexOf("&")));
        }

    }

}

