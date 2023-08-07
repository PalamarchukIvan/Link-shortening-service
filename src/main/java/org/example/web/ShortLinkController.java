package org.example.web;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.service.ShortLinkService.ShortLinkService;
import org.example.util.exceptions.ResourceNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Duration;
import java.time.Instant;
import java.util.regex.Pattern;

@Controller
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
public class ShortLinkController {
    private final ShortLinkService service;

    @GetMapping("/s/{hash}")
    public String getRealLink(@PathVariable String hash) {
        Instant start = Instant.now();
        String link;
        try {
            link = service.getByHash(hash).getLink();
        } catch (ResourceNotFoundException e) {
            service.updateOnStatistics(Duration.between(start, Instant.now()), hash, false);
            throw new ResourceNotFoundException();
        }
        if(!Pattern.compile("^[a-zA-Z]+://").matcher(link).find()) { //проверяет наличие каких либо латинских символов перед :// в начале ссылки
            service.updateOnStatistics(Duration.between(start, Instant.now()), hash, true);
            return  "redirect:/" + link;
        }

        return "redirect:" + link;
    }
}
