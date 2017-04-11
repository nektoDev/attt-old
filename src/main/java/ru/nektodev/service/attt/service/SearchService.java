package ru.nektodev.service.attt.service;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Service;
import ru.nektodev.service.attt.model.FoundedTorrent;
import ru.nektodev.service.attt.model.SearchResponse;
import ru.nektodev.service.attt.parser.TrackerParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author nektodev
 * @date 10/04/2017
 */
@Service
public class SearchService {

    private static final String LOGIN_URL = "https://rutracker.org/forum/login.php";
    private static final String RU_TRACKER_SEARCH_URL = "https://rutracker.org/forum/tracker.php?nm=";
    private HashMap<String, String> cookies = new HashMap<>();

    public SearchResponse search(String q) throws IOException {
        String searchUrl = RU_TRACKER_SEARCH_URL + q;

        TrackerParser parser = new TrackerParser();
        SearchResponse response = new SearchResponse();

        List<FoundedTorrent> rutracker = parser.parseSearch(searchUrl, this.cookies);

        if ((rutracker == null || rutracker.isEmpty()) && !isLoggedIn()) {
            initSession();
            rutracker = parser.parseSearch(searchUrl, this.cookies);
        }

        response.setRutracker(rutracker);

        return response;
    }

    private HashMap<String, String> getCookies() {
        if (!isLoggedIn()) {
            initSession();
        }
        return cookies;
    }

    private boolean isLoggedIn() {
        CloseableHttpClient client = HttpClients.createDefault();
        int statusCode = 200;
        try {
            HttpGet request = new HttpGet("https://rutracker.org/forum/login.php");
            request.setHeader("Cookie", "bb_session=" + cookies.get("bb_session"));
            statusCode = client.execute(request).getStatusLine().getStatusCode();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeClient(client);
        }

        return statusCode != 200 && getCookies() != null && !getCookies().isEmpty();
    }


    private void initSession() {
        System.out.println("Init session");

        CloseableHttpClient client = HttpClients.createDefault();
        try {
            HttpPost post = new HttpPost(LOGIN_URL);

            List<NameValuePair> urlParameters = new ArrayList<>();
            urlParameters.add(new BasicNameValuePair("login_username", "noctuliuz"));
            urlParameters.add(new BasicNameValuePair("login_password", "kHS4m@T9!f3lv"));
            urlParameters.add(new BasicNameValuePair("login", "%E2%F5%EE%E4"));
            post.setEntity(new UrlEncodedFormEntity(urlParameters));

            HttpResponse response = client.execute(post);
            String session = getSession(response.getHeaders("Set-Cookie"));

            cookies.put("bb_session", session);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeClient(client);
        }
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

    private void closeClient(CloseableHttpClient client) {
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
