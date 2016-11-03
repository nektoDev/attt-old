package ru.nektodev.service.attt.service;

import com.google.common.base.Strings;
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
import java.util.List;

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
    private NotificationFacade notification;

    @Scheduled(cron="${scheduler.import.cron}")
    public void checkTorrent() throws IOException {
        LOG.info("Start scheduled check.");
        for (TorrentInfo torrentInfo : torrentInfoRepository.findByAutoDownload(true)) {
            Document doc = Jsoup.connect(torrentInfo.getUrl()).get();
            Elements elements = doc.select("a");
            elements.attr("href");
            for (Element element : elements) {
                String magnet = element.attr("href");
                if (isMagnet(element)) {

                    if (!magnet.equalsIgnoreCase(torrentInfo.getMagnet())) {

                        String msg = String.format("New torrent for: %s\n %s", torrentInfo.getName(), torrentInfo.getUrl());
                        LOG.info(msg);
                        notify(torrentInfo, msg);

                        if (!Strings.isNullOrEmpty(transmissionService.addToTransmission(torrentInfo.getDownloadDir(), magnet))) {

                            LOG.info("Succesfully added: " + torrentInfo.getName() + " with magnet: " + magnet);
                            torrentInfo.setMagnet(magnet);
                            torrentInfoRepository.save(Collections.singletonList(torrentInfo));

                        } else {

                            String error = String.format("Error while add torrent: %s", torrentInfo.getName());
                            LOG.error(error);
                            notify(torrentInfo, error);

                        }
                    } else {
                        LOG.debug("Magnet for " + torrentInfo.getName() + " is the same: " + torrentInfo.getMagnet());
                    }
                }
            }

        }

        LOG.info("Scheduled check completed");
    }

    private void notify(TorrentInfo torrentInfo, String msg) {
        List<String> watchers = torrentInfo.getWatchers();
        for (String watcher : watchers) {
            notification.sendMessage(watcher, msg);
        }
    }

    private boolean isMagnet(Element element) {
        return element.hasAttr("href") && element.attr("href").startsWith("magnet:");
    }

}
