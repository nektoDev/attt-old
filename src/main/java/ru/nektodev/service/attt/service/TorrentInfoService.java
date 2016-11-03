package ru.nektodev.service.attt.service;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.nektodev.notification.api.NotificationFacade;
import ru.nektodev.service.attt.model.TorrentInfo;
import ru.nektodev.service.attt.parser.TrackerParser;
import ru.nektodev.service.attt.repository.TorrentInfoRepository;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author nektodev
 * @date 16/10/2016
 */
@Service
public class TorrentInfoService {
    private static Logger LOG = LoggerFactory.getLogger(TorrentInfoService.class);

    @Autowired
    private TorrentInfoRepository torrentInfoRepository;

    @Autowired
    private NotificationFacade facade;

    @Autowired
    private TransmissionService transmissionService;

    @Autowired
    private NotificationFacade notificationFacade;

    @Value("${default.watcher}")
    private String DEFAULT_WATCHER;

    public List<TorrentInfo> list() {
        List<TorrentInfo> all = torrentInfoRepository.findAll();
        StringBuilder sg = new StringBuilder("Watched torrents: \n");
        for (TorrentInfo torrentInfo : all) {
            sg.append(torrentInfo.getName()).append(" ").append(torrentInfo.getUrl()).append("\n");
        }
        facade.sendMessage("family", sg.toString());

        return all;
    }

    public List<TorrentInfo> add(List<TorrentInfo> torrentInfoList) throws IOException {
        for (TorrentInfo torrentInfo : torrentInfoList) {
            String hash;
            if (!Strings.isNullOrEmpty(torrentInfo.getMagnet())) {
                hash = transmissionService.addToTransmission(torrentInfo.getDownloadDir(), torrentInfo.getMagnet());
            } else {
                TrackerParser parser = new TrackerParser();
                try {
                    String magnet = parser.getMagnetFromUrl(torrentInfo.getUrl());
                    hash = transmissionService.addToTransmission(torrentInfo.getDownloadDir(), magnet);
                } catch (IOException e) {
                    LOG.error("Cannot get magnet link from: " + torrentInfo.getUrl(), e);
                    throw e;
                }
            }

            torrentInfo.setHash(hash);
            torrentInfo.setAdded(new Date());

            String message = "Torrent has been succesfully added: \n\n Hash:" + hash + "\nDownload directory: " + torrentInfo.getDownloadDir();
            if (torrentInfo.getWatchers() != null && !torrentInfo.getWatchers().isEmpty()) {
                for (String w : torrentInfo.getWatchers()) {
                    notificationFacade.sendMessage(w, message);
                }
            } else {
                notificationFacade.sendMessage(DEFAULT_WATCHER, message);
            }
        }

        return torrentInfoRepository.save(torrentInfoList);
    }

    public List<TorrentInfo> save(List<TorrentInfo> torrentInfoList) {
        return torrentInfoRepository.save(torrentInfoList);
    }

    public TorrentInfo delete(String id) {
        TorrentInfo ti = torrentInfoRepository.findOne(id);
        if (ti != null) {
            torrentInfoRepository.delete(id);
        }
        return ti;
    }
}
