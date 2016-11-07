package ru.nektodev.service.attt.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.nektodev.service.attt.model.TrackedTorrent;

/**
 * @author nektodev
 * @date 07/11/2016
 */
public interface TrackedTorrentRepository extends MongoRepository<TrackedTorrent, String> {
}
