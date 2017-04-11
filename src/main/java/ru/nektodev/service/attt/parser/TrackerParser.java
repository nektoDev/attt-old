package ru.nektodev.service.attt.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.nektodev.service.attt.model.FoundedTorrent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<FoundedTorrent> parseSearch(String searchUrl, HashMap<String, String> cookie) throws IOException {
        Document document = Jsoup.connect(searchUrl).cookies(cookie).get();
        Element table = document.body().getElementById("tor-tbl");
        if (table == null) {
            return null;
        }

        List<Element> elements = table.getElementsByTag("tr").stream()
                .filter(element -> element.getElementsByTag("td").size() == 10)
                .collect(Collectors.toList());
        if (elements == null || elements.isEmpty()) {
            return null;
        }

        List<FoundedTorrent> result = new ArrayList<>();
        for (Element element : elements) {
            Elements tds = element.getElementsByTag("td");

            FoundedTorrent t = new FoundedTorrent();

            t.setCategory(tds.get(2).select("div a").get(0).text());
            t.setName(tds.get(3).select("div a").get(0).text());
            t.setId(tds.get(3).select("div a").get(0).attr("data-topic_id"));
            t.setUrl("http://rutracker.org/forum/" + tds.get(3).select("div a").get(0).attr("href"));
            t.setSize(Long.parseLong(tds.get(5).getElementsByTag("u").text()));
            t.setSeeders(Integer.parseInt(tds.get(6).getElementsByTag("u").text()));

            result.add(t);
        }

        return result;
    }
}
