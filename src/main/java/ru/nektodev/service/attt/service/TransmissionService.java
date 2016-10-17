package ru.nektodev.service.attt.service;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.stereotype.Service;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author nektodev
 * @date 16/10/2016
 */
@Service
public class TransmissionService {


    public static final String TRANSMISSION_URL = "http://192.168.1.11:9091/transmission/rpc";

    public TransmissionService() {
    }

    public boolean addToTransmission(String downloadDir, String magnet) throws IOException {
        URL obj = new URL(TRANSMISSION_URL);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("POST");
        String session = getSession();
        System.out.println(session);
        con.addRequestProperty("X-Transmission-Session-Id", session);

        String urlParameters = "{\n" +
                "\t\"method\" : \"torrent-add\",\n" +
                "\t\"arguments\" : {\n" +
                "\t\t\"filename\":\"" + magnet + "\",\n" +
                "\t\t\"download-dir\": \""+ downloadDir +"\" \n" +
                "\t}\n" +
                "}";
        System.out.println(urlParameters);
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();

        System.out.println("[" + responseCode + "]");

        return 200 == responseCode;
    }

    private String getSession() throws IOException {

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(TRANSMISSION_URL);

        HttpResponse response = client.execute(post);
        System.out.println("Response Code : " +
                response.getStatusLine().getStatusCode());

        return response.getFirstHeader("X-Transmission-Session-Id").getValue();
    }
}
