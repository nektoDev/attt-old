package ru.nektodev.service.attt.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * @author nektodev
 * @date 03/11/2016
 */
public class TrackerParser {

    public String getMagnetFromUrl(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements elements = doc.select("a");
        elements.attr("href");

        for (Element element : elements) {
            String magnet = element.attr("href");
            if (isMagnet(element)) {
                return magnet;
            }
        }

        return null;
    }

    private boolean isMagnet(Element element) {
        return element.hasAttr("href") && element.attr("href").startsWith("magnet:");
    }
}
