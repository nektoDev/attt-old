package ru.nektodev.service.attt.service;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.nektodev.service.attt.model.TorrentInfo;
import ru.nektodev.service.attt.repository.TorrentInfoRepository;

import java.io.IOException;

/**
 * @author nektodev
 * @date 16/10/2016
 */
@Service
public class TorrentCheckScheduler {

    public static final Logger LOG = Logger.getLogger(TorrentCheckScheduler.class);

    @Autowired
    private TorrentInfoRepository torrentInfoRepository;

//    @Scheduled(cron="${scheduler.import.cron}")
    @Scheduled(fixedDelay = 5000)
    public void checkTorrent() throws IOException {
        LOG.info("Start scheduled check.");
        for (TorrentInfo torrentInfo : torrentInfoRepository.findAll()) {
            Document doc = Jsoup.connect(torrentInfo.getUrl()).get();
            Elements elements = doc.select("a");
            elements.attr("href");
            for (Element element : elements) {
                if (isMagnet(element) && !element.attr("href").equalsIgnoreCase(torrentInfo.getMagnet())) {
                    System.out.println(element.attr("href"));
                }
            }

        }

        LOG.info("Scheduled check completed");
    }

    private boolean isMagnet(Element element) {
        return element.hasAttr("href") && element.attr("href").startsWith("magnet:");
    }

}
