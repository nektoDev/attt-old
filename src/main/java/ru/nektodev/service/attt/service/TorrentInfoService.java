package ru.nektodev.service.attt.service;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nektodev.service.attt.model.TorrentInfo;
import ru.nektodev.service.attt.parser.TrackerParser;
import ru.nektodev.service.attt.repository.TorrentInfoRepository;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
    private TransmissionService transmissionService;

    @Autowired
    private NotificationService notificationService;

    public List<TorrentInfo> list() {
        return torrentInfoRepository.findAll();
    }

    public List<TorrentInfo> save(List<TorrentInfo> torrentInfoList) throws IOException {
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
            torrentInfo.setAddDate(new Date());

            String message = "Torrent has been successfully added: \n\n Hash:" + hash + "\nDownload directory: " + torrentInfo.getDownloadDir();
            notificationService.notify(torrentInfo.getWatchers(), message);
        }

        return torrentInfoRepository.save(torrentInfoList);
    }

    public TorrentInfo delete(String id) {
        TorrentInfo ti = torrentInfoRepository.findOne(id);
        if (ti != null) {
            torrentInfoRepository.delete(id);
        }
        return ti;
    }

    public TorrentInfo finish(String hash, String name) {
        List<TorrentInfo> torrentsByHash = torrentInfoRepository.findByHash(hash);

        Optional<TorrentInfo> torrentInfoOptional = torrentsByHash.stream()
                .filter(t -> t.getFinishDate() == null)
                .sorted((t1, t2) -> t2.getAddDate().compareTo(t1.getAddDate()))
                .findFirst();
        if (!torrentInfoOptional.isPresent()) return null;

        TorrentInfo torrent = torrentInfoOptional.get();

        torrent.setName(name);
        torrent.setFinishDate(new Date());

        String message = String.format("Torrent downloaded \n\nName: %s \n\nHash: %s", torrent.getName(), torrent.getHash());
        notificationService.notify(torrent.getWatchers(), message);

        return torrentInfoRepository.save(torrent);
    }
}
