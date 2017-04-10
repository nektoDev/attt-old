package ru.nektodev.service.attt.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.nektodev.service.attt.model.FinalizeRequest;
import ru.nektodev.service.attt.model.TorrentInfo;

import java.io.IOException;
import java.util.List;

/**
 * @author nektodev
 * @date 16/10/2016
 */
public interface TorrentFacade {

    ResponseEntity<List<TorrentInfo>> list();

    ResponseEntity<TorrentInfo> get(@PathVariable String id);

    ResponseEntity save(List<TorrentInfo> torrentInfoList) throws IOException;

    ResponseEntity<TorrentInfo> finalizeTorrent(FinalizeRequest request);

    TorrentInfo delete(@RequestParam(required = true) String id);
}
