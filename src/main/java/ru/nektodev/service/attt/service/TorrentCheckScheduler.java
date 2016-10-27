package ru.nektodev.service.attt.service;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.nektodev.notification.api.NotificationFacade;
import ru.nektodev.service.attt.model.TorrentInfo;
import ru.nektodev.service.attt.repository.TorrentInfoRepository;

import java.io.IOException;
import java.util.Collections;

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
    TransmissionService transmissionService;

    @Autowired
    private NotificationFacade notification;

    @Scheduled(cron="${scheduler.import.cron}")
    public void checkTorrent() throws IOException {
        LOG.info("Start scheduled check.");
        for (TorrentInfo torrentInfo : torrentInfoRepository.findAll()) {
            Document doc = Jsoup.connect(torrentInfo.getUrl()).get();
            Elements elements = doc.select("a");
            elements.attr("href");
            for (Element element : elements) {
                String magnet = element.attr("href");
                if (isMagnet(element)) {

                    if (!magnet.equalsIgnoreCase(torrentInfo.getMagnet())) {

                        LOG.info("New torrent for: " + torrentInfo.getName());
                        String msg = String.format("New torrent for: %s\n %s", torrentInfo.getName(), torrentInfo.getUrl());
                        notification.sendMessage("family", msg);

                        if (transmissionService.addToTransmission(torrentInfo.getDownloadDir(), magnet)) {

                            LOG.info("Succesfully added: " + torrentInfo.getName() + " with magnet: " + magnet);
                            torrentInfo.setMagnet(magnet);
                            torrentInfoRepository.save(Collections.singletonList(torrentInfo));

                        } else {

                            LOG.error("Error while add torrent: " + torrentInfo.getName());
                            notification.sendMessage("family", "Error while add torrent: " + torrentInfo.getName());

                        }
                    } else {
                        LOG.debug("Magnet for " + torrentInfo.getName() + " is the same: " + torrentInfo.getMagnet());
                    }
                }
            }

        }

        LOG.info("Scheduled check completed");
    }

    private boolean isMagnet(Element element) {
        return element.hasAttr("href") && element.attr("href").startsWith("magnet:");
    }

}
