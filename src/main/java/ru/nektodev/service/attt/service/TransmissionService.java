package ru.nektodev.service.attt.service;

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
        con.addRequestProperty("X-Transmission-Session-Id", getSession());
        con.addRequestProperty("Content-type", "application/json");

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
        URL obj = new URL(TRANSMISSION_URL);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("POST");
        con.addRequestProperty("Content-type", "application/json");

        return con.getHeaderField("X-Transmission-Session-Id");
    }
}
