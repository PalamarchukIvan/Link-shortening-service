package org.example.web;

import lombok.RequiredArgsConstructor;
import org.example.model.dto.ShortLinkRequestDto;
import org.example.model.dto.ShortLinkResponseDto;
import org.example.service.ShortLinkService;
import org.example.util.Mapstruct.ShortLinkMapper;
import org.example.util.exceptions.NoProtocolMentionedException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Pattern;

@RestController
@RequestMapping(value = "/shortening_api", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ShortLinkRestController {
    private final ShortLinkService service;
    private final ShortLinkMapper mapper;

    @PostMapping("/create")
    public ShortLinkResponseDto saveShortLink(@RequestBody ShortLinkRequestDto link) {
        if(!Pattern.compile("^[a-zA-Z]+://").matcher(link.link).find()) { //проверяет наличие каких либо латинских символов перед :// в начале ссылки
            throw new NoProtocolMentionedException();
        }
        return
                mapper.toResponse(
                        service.create(
                                mapper.fromRequest(link)
                        ));
    }

    @DeleteMapping("/{hash}")
    public ShortLinkResponseDto deleteLink(@PathVariable String hash) {
        return
                mapper.toResponse(
                        service.deleteByHash(hash)
                );
    }
}
