package ru.nektodev.service.attt.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.nektodev.service.attt.model.TorrentInfo;

import java.io.IOException;
import java.util.List;

/**
 * @author nektodev
 * @date 16/10/2016
 */
public interface TorrentInfoFacade {

    public List<TorrentInfo> list();

    public ResponseEntity<List<TorrentInfo>> add(List<TorrentInfo> torrentInfoList) throws IOException;

    ResponseEntity<String> update(@RequestBody List<TorrentInfo> torrentInfoList);

    void finishDownload(@RequestBody TorrentInfo torrentInfo);

    TorrentInfo delete(@RequestBody String id);

    void forceCheck() throws IOException;
}
