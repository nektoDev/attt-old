package ru.nektodev.service.attt.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.nektodev.service.attt.model.FinalizeRequest;
import ru.nektodev.service.attt.model.TorrentInfo;

import java.util.List;

/**
 * @author nektodev
 * @date 07/11/2016
 */
public interface TorrentFacade {

    ResponseEntity<List<TorrentInfo>> listTorrents();

    @RequestMapping(method = RequestMethod.GET, path = "/tracked")
    ResponseEntity<List<TorrentInfo>> listTrackedTorrents();

    ResponseEntity<List<TorrentInfo>> addTorrents(List<TorrentInfo> torrentInfoList, boolean isTracked);

    ResponseEntity<List<TorrentInfo>> removeTorrents(List<String> torrentIds);

    ResponseEntity<TorrentInfo> finalizeTorrent(FinalizeRequest request);

    void updateTracked();
}
