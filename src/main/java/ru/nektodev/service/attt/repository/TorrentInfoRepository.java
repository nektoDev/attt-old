package ru.nektodev.service.attt.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.nektodev.service.attt.model.TorrentInfo;

import java.util.List;

/**
 * @author nektodev
 * @date 16/10/2016
 */
public interface TorrentInfoRepository extends MongoRepository<TorrentInfo, String> {

    List<TorrentInfo> findByTracked(boolean autoDownload);

    List<TorrentInfo> findByHash(String hash);
}
