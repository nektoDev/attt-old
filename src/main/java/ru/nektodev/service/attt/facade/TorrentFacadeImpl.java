package ru.nektodev.service.attt.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.nektodev.service.attt.api.TorrentFacade;
import ru.nektodev.service.attt.model.FinalizeRequest;
import ru.nektodev.service.attt.model.TorrentInfo;
import ru.nektodev.service.attt.service.TorrentService;

import java.util.List;

/**
 * @author nektodev
 * @date 07/11/2016
 */
@RestController()
@RequestMapping("/torrent")
public class TorrentFacadeImpl implements TorrentFacade {

    @Autowired
    private TorrentService service;

    @Override
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<TorrentInfo>> listTorrents() {
        return ResponseEntity.ok(service.list());
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, path = "/tracked")
    public ResponseEntity<List<TorrentInfo>> listTrackedTorrents() {
        return ResponseEntity.ok(service.listTracked());
    }

    @Override
    public ResponseEntity<List<TorrentInfo>> addTorrents(List<TorrentInfo> torrentInfoList, boolean isTracked) {
        return null;
    }

    @Override
    public ResponseEntity<List<TorrentInfo>> removeTorrents(List<String> torrentIds) {
        return null;
    }

    @Override
    public ResponseEntity<TorrentInfo> finalizeTorrent(FinalizeRequest request) {
        return null;
    }


    @Override
    public void updateTracked() {

    }
}
