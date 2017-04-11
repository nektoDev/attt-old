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
import org.springframework.stereotype.Service;
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

    public SearchService() {
        initSession();
    }

    public SearchResponse search(String q) throws IOException {
        String searchUrl = RU_TRACKER_SEARCH_URL + q;

        TrackerParser parser = new TrackerParser();
        SearchResponse response = new SearchResponse();
        response.setRutracker(parser.parseSearch(searchUrl, cookies));

        return response;
    }

    private void initSession() {
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
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
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


}
