package org.example.web;

import lombok.RequiredArgsConstructor;
import org.example.model.dto.ShortLinkRequestDto;
import org.example.model.dto.ShortLinkResponseDto;
import org.example.service.ShortLinkService.ShortLinkService;
import org.example.util.Mapstruct.ShortLinkMapper;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/shortening_api", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
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
