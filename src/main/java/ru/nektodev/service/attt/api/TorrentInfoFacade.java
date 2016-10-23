package ru.nektodev.service.attt.api;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.nektodev.service.attt.model.TorrentInfo;

import java.util.List;

/**
 * @author nektodev
 * @date 16/10/2016
 */
public interface TorrentInfoFacade {

    public List<TorrentInfo> list();

    public List<TorrentInfo> save(List<TorrentInfo> torrentInfoList);

    TorrentInfo delete(@RequestBody TorrentInfo id);
}
