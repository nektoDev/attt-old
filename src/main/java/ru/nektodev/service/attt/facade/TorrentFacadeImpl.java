package ru.nektodev.service.attt.facade;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
@CrossOrigin
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
    @RequestMapping(method = RequestMethod.GET, path = "{id}")
    public ResponseEntity<TorrentInfo> get(@PathVariable String id) {
        if (Strings.isNullOrEmpty(id)) {
            ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(service.get(id));
    }

    @Override
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity save(@RequestBody List<TorrentInfo> torrentInfoList) throws IOException {
        String validateResult = validateAddTorrent(torrentInfoList);
        if (!Strings.isNullOrEmpty(validateResult)) {
            return ResponseEntity.badRequest().body(validateResult);
        }

        return ResponseEntity.ok(service.save(torrentInfoList));
    }

    @Override
    @RequestMapping(method = RequestMethod.POST, path = "/finalize")
    public ResponseEntity<TorrentInfo> finalizeTorrent(@RequestBody FinalizeRequest request) {
        return ResponseEntity.ok(service.finish(request.getHash(), request.getName()));
    }

    @Override
    @RequestMapping(method = RequestMethod.DELETE)
    public TorrentInfo delete(@RequestParam String id) {
        return service.delete(id);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, path = "/forceCheck/{id}")
    public ResponseEntity forceCheck(@PathVariable(required = false) String id) throws IOException {
        if (Strings.isNullOrEmpty(id)) {
            return ResponseEntity.ok(torrentChecker.checkTorrents());
        }
        TorrentInfo torrentInfo = service.get(id);
        if (torrentInfo == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(torrentChecker.checkTorrent(torrentInfo));
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
