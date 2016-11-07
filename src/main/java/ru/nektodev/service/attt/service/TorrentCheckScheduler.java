package ru.nektodev.service.attt.service;

import com.google.common.base.Strings;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.nektodev.service.attt.model.TorrentInfo;
import ru.nektodev.service.attt.parser.TrackerParser;
import ru.nektodev.service.attt.repository.TorrentInfoRepository;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;

/**
 * @author nektodev
 * @date 16/10/2016
 */
@Service
public class TorrentCheckScheduler {

    private static final Logger LOG = Logger.getLogger(TorrentCheckScheduler.class);

    @Autowired
    private TorrentInfoRepository torrentInfoRepository;

    @Autowired
    private TransmissionService transmissionService;

    @Autowired
    private NotificationService notification;

    @Scheduled(cron = "${scheduler.import.cron}")
    public void checkTorrent() throws IOException {
        LOG.info("Start scheduled check.");
        TrackerParser parser = new TrackerParser();
        Date lastCheckDate = new Date();

        for (TorrentInfo torrentInfo : torrentInfoRepository.findByTracked(true)) {
            String magnet = parser.getMagnetFromUrl(torrentInfo.getUrl());
            torrentInfo.setLastCheckDate(lastCheckDate);

            if (!magnet.equalsIgnoreCase(torrentInfo.getMagnet())) {

                String msg = String.format("New torrent for: %s\n %s", torrentInfo.getName(), torrentInfo.getUrl());
                LOG.info(msg);
                notification.notify(torrentInfo.getWatchers(), msg);

                torrentInfo = sendToTransmission(torrentInfo, magnet);

            } else {
                LOG.debug("Magnet for " + torrentInfo.getName() + " is the same: " + torrentInfo.getMagnet());
            }

            torrentInfoRepository.save(Collections.singletonList(torrentInfo));
        }

        LOG.info("Scheduled check completed");
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
