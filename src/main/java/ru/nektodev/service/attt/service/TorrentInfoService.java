package ru.nektodev.service.attt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nektodev.notification.api.NotificationFacade;
import ru.nektodev.service.attt.model.TorrentInfo;
import ru.nektodev.service.attt.repository.TorrentInfoRepository;

import java.util.List;

/**
 * @author nektodev
 * @date 16/10/2016
 */
@Service
public class TorrentInfoService {
    @Autowired
    private TorrentInfoRepository torrentInfoRepository;

    @Autowired
    private NotificationFacade facade;

    public List<TorrentInfo> list() {
        List<TorrentInfo> all = torrentInfoRepository.findAll();
        StringBuilder sg = new StringBuilder("Watched torrents: \n");
        for (TorrentInfo torrentInfo : all) {
            sg.append(torrentInfo.getName()).append(" ").append(torrentInfo.getUrl()).append("\n");
        }
        facade.sendMessage("family", sg.toString());

        return all;
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
