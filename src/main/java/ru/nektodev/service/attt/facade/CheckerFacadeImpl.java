package ru.nektodev.service.attt.facade;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nektodev.service.attt.api.CheckerFacade;
import ru.nektodev.service.attt.service.TorrentCheckerService;

import java.io.IOException;

/**
 * @author nektodev
 * @date 10/04/2017
 */
@CrossOrigin
@RestController()
@RequestMapping("/checker")
public class CheckerFacadeImpl implements CheckerFacade {

    @Autowired
    private TorrentCheckerService torrentCheckerService;

    @Override
    @RequestMapping(method = RequestMethod.GET, path = "{id}")
    public ResponseEntity check(@PathVariable String id) throws IOException {
        if (Strings.isNullOrEmpty(id)) {
            return ResponseEntity.badRequest().body("Torrent Id must not be empty.");
        }
        return ResponseEntity.ok(torrentCheckerService.checkTorrent(id));
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, path = "")
    public ResponseEntity checkAll() throws IOException {
        return ResponseEntity.ok(torrentCheckerService.checkTorrents());
    }
}
