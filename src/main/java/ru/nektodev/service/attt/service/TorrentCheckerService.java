package ru.nektodev.service.attt.service;

import com.google.common.base.Strings;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nektodev.service.attt.model.TorrentInfo;
import ru.nektodev.service.attt.parser.TrackerParser;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;

/**
 * @author nektodev
 * @date 10/04/2017
 */
@Service
public class TorrentCheckerService {
    private static final Logger LOG = Logger.getLogger(TorrentCheckerService.class);

    @Autowired
    private TorrentInfoService torrentInfoService;

    @Autowired
    private NotificationService notification;

    @Autowired
    private TransmissionService transmissionService;

    public String checkTorrents() throws IOException {
        TrackerParser parser = new TrackerParser();
        Date lastCheckDate = new Date();
        StringBuilder result = new StringBuilder();
        for (TorrentInfo torrentInfo : torrentInfoService.getAllTracked()) {
            result.append(checkTorrent(parser, lastCheckDate, torrentInfo)).append("\n");
        }
        return result.toString();
    }

    public String checkTorrent(TorrentInfo torrentInfo) throws IOException {
        return checkTorrent(new TrackerParser(), new Date(), torrentInfo);
    }

    private String checkTorrent(TrackerParser parser, Date lastCheckDate, TorrentInfo torrentInfo) throws IOException {
        String magnet = parser.getMagnetFromUrl(torrentInfo.getUrl());
        torrentInfo.setLastCheckDate(lastCheckDate);
        String msg;

        if (magnet == null) {
            msg = String.format("Incorrect URL: %s\n %s", torrentInfo.getName(), torrentInfo.getUrl());
            LOG.warn(msg);
            notification.notify(torrentInfo.getWatchers(), msg);
            return msg;
        }

        if (!magnet.equalsIgnoreCase(torrentInfo.getMagnet())) {

            msg = String.format("New torrent for: %s\n %s", torrentInfo.getName(), torrentInfo.getUrl());
            LOG.info(msg);
            notification.notify(torrentInfo.getWatchers(), msg);

            torrentInfo = sendToTransmission(torrentInfo, magnet);

        } else {
            msg = "Magnet for " + torrentInfo.getName() + " is the same: " + torrentInfo.getMagnet();
            LOG.debug("Magnet for " + torrentInfo.getName() + " is the same: " + torrentInfo.getMagnet());
        }

        torrentInfoService.save(Collections.singletonList(torrentInfo));

        return msg;
    }

    private TorrentInfo sendToTransmission(TorrentInfo torrentInfo, String magnet) {
        String hash = transmissionService.addToTransmission(torrentInfo.getDownloadDir(), magnet);
        if (!Strings.isNullOrEmpty(hash)) {

            LOG.info("Succesfully added: " + torrentInfo.getName() + " with magnet: " + magnet);
            torrentInfo.setMagnet(magnet);
            torrentInfo.setHash(hash);
            torrentInfo.setAddDate(null);
            torrentInfo.setFinishDate(null);
            torrentInfo.setLastUpdateDate(new Date());

        } else {

            String error = String.format("Error while save torrent: %s", torrentInfo.getName());
            LOG.error(error);
            notification.notify(torrentInfo.getWatchers(), error);

        }
        return torrentInfo;
    }
}
