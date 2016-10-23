package ru.nektodev.service.attt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    public List<TorrentInfo> list() {
        return torrentInfoRepository.findAll();
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
