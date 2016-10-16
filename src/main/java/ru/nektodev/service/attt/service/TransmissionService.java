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
        con.addRequestProperty("X-Transmission-Session-Id", "ufYKhw26nZ6Kgafw7kpDX7F5IG4P9ie7RlSc4q3fXe4IJHuF");
        con.addRequestProperty("Content-type", "");

        String urlParameters = "{\n" +
                "\t\"method\" : \"torrent-add\",\n" +
                "\t\"arguments\" : {\n" +
                "\t\t\"filename\":\"" + magnet + "\",\n" +
                "\t\t\"download-dir\": \""+ downloadDir +"\" \n" +
                "\t}\n" +
                "}";

        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();

        return 200 == responseCode;
    }
}
