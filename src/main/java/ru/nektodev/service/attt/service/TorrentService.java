package ru.nektodev.service.attt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nektodev.service.attt.model.TorrentInfo;
import ru.nektodev.service.attt.repository.TorrentInfoRepository;
import ru.nektodev.service.attt.repository.TrackedTorrentRepository;

import java.util.List;

/**
 * @author nektodev
 * @date 07/11/2016
 */
@Service
public class TorrentService {

    @Autowired
    private TorrentInfoRepository infoRepository;

    @Autowired
    private TrackedTorrentRepository trackedRepository;

    public List<TorrentInfo> list() {
        return infoRepository.findAll();
    }

    public List<TorrentInfo> listTracked() {
        return infoRepository.findAll();
    }
}
