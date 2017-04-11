package ru.nektodev.service.attt.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.nektodev.service.attt.api.SearchFacade;
import ru.nektodev.service.attt.model.SearchResponse;
import ru.nektodev.service.attt.service.SearchService;

import java.io.IOException;

/**
 * @author nektodev
 * @date 10/04/2017
 */
@RestController
@CrossOrigin
@RequestMapping("/search")
public class SearchFacadeImpl implements SearchFacade {

    @Autowired
    private SearchService service;

    @Override
    @RequestMapping(method = RequestMethod.GET)
    public SearchResponse search(@RequestParam String q) throws IOException {
        return service.search(q);
    }
}
