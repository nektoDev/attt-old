package ru.nektodev.service.attt.model;

import java.io.Serializable;

/**
 * @author nektodev
 * @date 16/10/2016
 */
public class TorrentInfo implements Serializable {
    private String url;
    private String magnet;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMagnet() {
        return magnet;
    }

    public void setMagnet(String magnet) {
        this.magnet = magnet;
    }
}
