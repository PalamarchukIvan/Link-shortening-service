package org.example.web;

import lombok.RequiredArgsConstructor;
import org.example.model.dto.ShortLinkResponseDto;
import org.example.service.ShortLinkService;
import org.example.util.Mapstruct.ShortLinkMapper;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/shortening_api", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@PreAuthorize("authentication.principal.username == 'dev'")
public class ShortLinkRestController {
    private final ShortLinkService service;
    private final ShortLinkMapper mapper;

    @DeleteMapping("/{hash}")
    public ShortLinkResponseDto deleteLink(@PathVariable String hash) {
        return
                mapper.toResponse(
                        service.deleteByHash(hash)
                );
    }
}
