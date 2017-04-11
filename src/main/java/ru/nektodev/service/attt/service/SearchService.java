package ru.nektodev.service.attt.service;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import ru.nektodev.service.attt.model.FoundedTorrent;
import ru.nektodev.service.attt.model.SearchResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author nektodev
 * @date 10/04/2017
 */
@Service
public class SearchService {

    public SearchResponse search(String q) throws IOException {
        String url = "https://rutracker.org/forum/login.php";
        String serachurl = "https://rutracker.org/forum/tracker.php?nm=" + q;
        CloseableHttpClient client = HttpClients.createDefault();
        try {
            HttpPost post = new HttpPost(url);

            List<NameValuePair> urlParameters = new ArrayList<>();
            urlParameters.add(new BasicNameValuePair("login_username", "noctuliuz"));
            urlParameters.add(new BasicNameValuePair("login_password", "kHS4m@T9!f3lv"));
            urlParameters.add(new BasicNameValuePair("login", "%E2%F5%EE%E4"));
            post.setEntity(new UrlEncodedFormEntity(urlParameters));

            HttpResponse response = client.execute(post);
            String session = getSession(response.getHeaders("Set-Cookie"));

            HashMap<String, String> cookie = new HashMap<>();
            cookie.put("bb_session", session);
            Document document = Jsoup.connect(serachurl).cookies(cookie).get();

            Element table = document.body().getElementById("tor-tbl");
            List<Element> elements = table.getElementsByTag("tr").stream()
                    .filter(element -> element.getElementsByTag("td").size() == 10)
                    .collect(Collectors.toList());

            List<FoundedTorrent> result = new ArrayList<>();
            for (Element element : elements) {
                FoundedTorrent t = new FoundedTorrent();
                Elements tds = element.getElementsByTag("td");

                System.out.println(tds.get(5).getElementsByTag("u").text());

                t.setCategory(tds.get(2).select("div a").get(0).text());
                t.setName(tds.get(3).select("div a").get(0).text());
                t.setId(tds.get(3).select("div a").get(0).attr("data-topic_id"));
                t.setUrl("http://rutracker.org/forum/" + tds.get(3).select("div a").get(0).attr("href"));
                t.setSize(Long.parseLong(tds.get(5).getElementsByTag("u").text()));
                t.setSeeders(Integer.parseInt(tds.get(6).getElementsByTag("u").text()));

                System.out.println(t);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    private String getSession(Header[] headers) {
        for (Header header : headers) {
            for (int j = 0; j < header.getElements().length; j++) {
                HeaderElement element = header.getElements()[j];
                if ("bb_session".equalsIgnoreCase(element.getName())) {
                    return element.getValue();
                }
            }
        }
        return null;
    }


}
