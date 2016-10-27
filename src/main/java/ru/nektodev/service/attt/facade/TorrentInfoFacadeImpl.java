package ru.nektodev.service.attt.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.nektodev.service.attt.api.TorrentInfoFacade;
import ru.nektodev.service.attt.model.TorrentInfo;
import ru.nektodev.service.attt.service.TorrentCheckScheduler;
import ru.nektodev.service.attt.service.TorrentInfoService;

import java.io.IOException;
import java.util.List;

/**
 * @author nektodev
 * @date 16/10/2016
 */
@RestController()
@RequestMapping("/torrents")
public class TorrentInfoFacadeImpl implements TorrentInfoFacade {

    @Autowired
    private TorrentInfoService service;

    @Autowired
    private TorrentCheckScheduler torrentChecker;

    @Override
    @RequestMapping(method = RequestMethod.GET)
    public List<TorrentInfo> list() {
        return service.list();
    }

    @Override
    @RequestMapping(method = RequestMethod.POST)
    public List<TorrentInfo> save(@RequestBody List<TorrentInfo> torrentInfoList) {
        return service.save(torrentInfoList);
    }

    @Override
    @RequestMapping(method = RequestMethod.DELETE)
    public TorrentInfo delete(@RequestBody TorrentInfo id) {
        return service.delete(id.getUrl());
    }

    @Override
    @RequestMapping(method = RequestMethod.GET)
    public void forceCheck() throws IOException {
        torrentChecker.checkTorrent();
    }

}
