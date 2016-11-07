package ru.nektodev.service.attt.facade;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.nektodev.service.attt.api.TorrentFacade;
import ru.nektodev.service.attt.model.FinalizeRequest;
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
@RequestMapping("/torrent")
public class TorrentFacadeImpl implements TorrentFacade {

    private static final String ERROR_URL = "Rutracker URL or magnet must be provided";
    private static final String ERROR_DOWNLOAD_DIR = "Download directory must be provided";
    private static final String ERROR_HASH_OR_ID = "Torrent id or hash must be provided";

    @Autowired
    private TorrentInfoService service;

    @Autowired
    private TorrentCheckScheduler torrentChecker;

    @Override
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<TorrentInfo>> list() {
        return ResponseEntity.ok(service.list());
    }

    @Override
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity save(List<TorrentInfo> torrentInfoList) throws IOException {
        String validateResult = validateAddTorrent(torrentInfoList);
        if (!Strings.isNullOrEmpty(validateResult)) {
            return ResponseEntity.badRequest().body(validateResult);
        }

        return ResponseEntity.ok(service.save(torrentInfoList));
    }

    @Override
    public ResponseEntity<TorrentInfo> finalizeTorrent(FinalizeRequest request) {
        return ResponseEntity.ok(service.finish(request.getHash(), request.getName()));
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
}