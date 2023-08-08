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

        service.updateOnStatistics(Duration.between(start, Instant.now()), hash, true);
        if(Pattern.compile("^[a-zA-Z0-9]+://").matcher(link).find() || link.startsWith("//")) { // Если указан протокол - идем с ним в Интернет
            return "redirect:" + link;
        }
        if(link.startsWith("/")) { // Если ссылка начинается с '/', то пускаем ссылку внутри нашего сервиса
            return "redirect:" + link;
        }
        return "redirect://" + link; // Если протокол не указан, то идем во внешнею сеть и даем выбор протокола на откуп автоматике
    }
}
