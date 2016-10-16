package ru.nektodev.service.attt.service;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author nektodev
 * @date 16/10/2016
 */
@Service
public class TorrentCheckScheduler {

    public static final Logger LOG = Logger.getLogger(TorrentCheckScheduler.class);

    @Scheduled(cron="${scheduler.import.cron}")
    public void checkTorrent() {
        LOG.info("Start scheduled check.");
        LOG.info("Scheduled check completed");
    }

}
