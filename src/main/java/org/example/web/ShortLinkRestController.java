package org.example.web;

import lombok.RequiredArgsConstructor;
import org.example.model.ShortLink;
import org.example.service.ShortLinkService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/shortening_api", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ShortLinkRestController {
    private final ShortLinkService service;
    @PostMapping("/create")
    public ShortLink saveShortLink(@RequestBody ShortLink link) {
        return service.create(link);
    }
    @DeleteMapping("/")
    public ShortLink deleteLink(Long id) {
        return service.deleteById(id);
    }
}
