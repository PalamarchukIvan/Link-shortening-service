package org.example.web;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.ShortLink;
import org.example.model.User;
import org.example.model.dto.ShortLinkRequestDto;
import org.example.service.ShortLinkService;
import org.example.util.CurrentUserUtil;
import org.example.util.Mapstruct.ShortLinkMapper;
import org.example.util.exceptions.ResourceNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;

@Controller
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("isAuthenticated()")
public class ShortLinkController {
    private final ShortLinkService service;
    private final ShortLinkMapper mapper;

    @GetMapping("/s/{hash}")
    public String getRealLink(@PathVariable String hash) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Instant start = Instant.now();
        String link;
        try {
            link = service.getByHash(hash).getLink();
        } catch (ResourceNotFoundException e) {
            service.updateOnStatistics(Duration.between(start, Instant.now()), hash, user, false);
            throw new ResourceNotFoundException();
        }
        service.updateOnStatistics(Duration.between(start, Instant.now()), hash, user, true);
        return "redirect:" + link;
    }

    @GetMapping("/s/create")
    public String createLinkEndpoint() {
        return "create_link";
    }

    @PostMapping("/s/create")
    public String saveShortLink(@ModelAttribute ShortLinkRequestDto linkDto, Model model) {
        model.addAttribute("new_link",
                mapper.toResponse(
                    service.create(
                            mapper.fromRequest(linkDto)
                )));
        return "create_link";
    }
    @PostMapping("/s/delete/")
    public String deleteByHash(@RequestParam String hash) {
        service.deleteByHash(hash);
        return "redirect:../../main";
    }
}
