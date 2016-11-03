package ru.nektodev.service.attt.facade;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
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

    public static final String ERROR_URL = "Rutracker URL or magnet must be provided";
    public static final String ERROR_DOWNLOAD_DIR = "Download directory must be provided";
    public static final String ERROR_HASH_OR_ID = "Torrent id or hash must be provided";
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
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity add(@RequestBody List<TorrentInfo> torrentInfoList) throws IOException {
        String validateResult = validateAddTorrent(torrentInfoList);
        if (!Strings.isNullOrEmpty(validateResult)) {
            return ResponseEntity.badRequest().body(validateResult);
        }

        return ResponseEntity.ok(service.add(torrentInfoList)); //TODO should return ResponseEntity.created
    }

    @Override
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity update(@RequestBody List<TorrentInfo> torrentInfoList) {
        String validateResult = validateUpdateTorrent(torrentInfoList);
        if (!Strings.isNullOrEmpty(validateResult)) {
            return ResponseEntity.badRequest().body(validateResult);
        }
        return ResponseEntity.ok(service.save(torrentInfoList));
    }

    @Override
    @RequestMapping(path = "/finish", method = RequestMethod.POST)
    public void finishDownload(@RequestBody TorrentInfo torrentInfo) {
        service.finish(torrentInfo);
    }

    @Override
    @RequestMapping(method = RequestMethod.DELETE)
    public TorrentInfo delete(@RequestBody String id) {
        return service.delete(id);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, path = "/forceCheck")
    public void forceCheck() throws IOException {
        torrentChecker.checkTorrent();
    }

    private String validateAddTorrent(List<TorrentInfo> torrentInfoList) {
        StringBuilder result = new StringBuilder();

        for (TorrentInfo torrentInfo : torrentInfoList) {
            if (Strings.isNullOrEmpty(torrentInfo.getUrl()) && Strings.isNullOrEmpty(torrentInfo.getMagnet())) {
                result.append(torrentInfo.getName()).append(" ").append(ERROR_URL);
            }
            if (Strings.isNullOrEmpty(torrentInfo.getDownloadDir())) {
                result.append(torrentInfo.getName()).append(" ").append(ERROR_DOWNLOAD_DIR);
            }
        }

        return result.toString();
    }

    private String validateUpdateTorrent(List<TorrentInfo> torrentInfoList) {
        StringBuilder result = new StringBuilder();

        torrentInfoList.stream()
                .filter(torrentInfo -> Strings.isNullOrEmpty(torrentInfo.getId()) && Strings.isNullOrEmpty(torrentInfo.getHash()))
                .forEach(torrentInfo ->
                        result.append(torrentInfo.getName())
                                .append(" ")
                                .append(ERROR_HASH_OR_ID)
                );

        return result.toString();
    }

}
