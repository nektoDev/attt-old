package ru.nektodev.service.attt.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author nektodev
 * @date 16/10/2016
 */
@Service
public class TorrentCheckScheduler {

    private static final Logger LOG = Logger.getLogger(TorrentCheckScheduler.class);

    @Autowired
    private TorrentCheckerService torrentCheckerService;

    @Scheduled(cron = "${scheduler.import.cron}")
    private void scheduledCheck() throws IOException {
        LOG.info("Start scheduled check.");
        torrentCheckerService.checkTorrents();
        LOG.info("Scheduled check completed");
    }
}
