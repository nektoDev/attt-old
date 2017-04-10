package ru.nektodev.service.attt.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;

/**
 * @author nektodev
 * @date 10/04/2017
 */
public interface CheckerFacade {

    ResponseEntity check(@PathVariable String id) throws IOException;

    ResponseEntity checkAll() throws IOException;
}
