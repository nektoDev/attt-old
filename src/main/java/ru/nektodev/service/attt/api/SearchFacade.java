package ru.nektodev.service.attt.api;

import ru.nektodev.service.attt.model.SearchResponse;

import java.io.IOException;

/**
 * @author nektodev
 * @date 10/04/2017
 */
public interface SearchFacade {
    SearchResponse search(String q) throws IOException;
}
