package org.example.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.service.ShortLinkService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Duration;
import java.time.Instant;

@Controller
@RequestMapping(value = "/shortening_api", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
public class ShortLinkController {
    private final ShortLinkService service;

    @GetMapping("/{hash}")
    public String getRealLink(@PathVariable String hash) {
        Instant start = Instant.now();
        String link = service.getByHash(hash).getLink();
        Instant end = Instant.now();
        service.updateOnStatistics(end, Duration.between(start, end), hash);
        return "redirect:" + link;
    }
}
