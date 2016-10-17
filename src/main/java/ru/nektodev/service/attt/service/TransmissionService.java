package ru.nektodev.service.attt.service;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author nektodev
 * @date 16/10/2016
 */
@Service
public class TransmissionService {


    public static final String TRANSMISSION_URL = "http://192.168.1.11:9091/transmission/rpc";

    public TransmissionService() {
    }

    public boolean addToTransmission(String downloadDir, String magnet) {
        CloseableHttpClient client = HttpClients.createDefault();
        try {
            HttpPost post = new HttpPost(TRANSMISSION_URL);
            post.addHeader("X-Transmission-Session-Id", getSession());
            String body = "{\n" +
                    "\t\"method\" : \"torrent-add\",\n" +
                    "\t\"arguments\" : {\n" +
                    "\t\t\"filename\":\"" + magnet + "\",\n" +
                    "\t\t\"download-dir\": \"" + downloadDir + "\" \n" +
                    "\t}\n" +
                    "}";
            StringEntity params = new StringEntity(body);
            post.setEntity(params);
            HttpResponse response = client.execute(post);

            int responseCode = response.getStatusLine().getStatusCode();

            System.out.println("[" + responseCode + "]");

            return 200 == responseCode;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getSession() {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(TRANSMISSION_URL);
        try {
            HttpResponse response = client.execute(post);
            return response.getFirstHeader("X-Transmission-Session-Id").getValue();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
