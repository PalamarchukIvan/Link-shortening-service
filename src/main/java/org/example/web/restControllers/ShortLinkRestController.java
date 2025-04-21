package org.example.web.restControllers;

import lombok.AllArgsConstructor;
import org.example.dto.ShortLinkUpdateRequestDto;
import org.example.facade.ShortLinkControllerFacade;
import org.example.dto.ShortLinkDto;
import org.example.dto.ShortLinkRequestDto;
import org.example.web.ResultWithStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
@AllArgsConstructor
@RequestMapping(value = "/rest/short-links", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("isAuthenticated()")
public class ShortLinkRestController {

    private final ShortLinkControllerFacade shortLinkServiceFacade;

    @GetMapping("/")
    public ResultWithStatus<List<ShortLinkDto>> getShortLinksByUser() {
        return shortLinkServiceFacade.getShortLinksByUser();
    }

    @PostMapping("/create")
    public ResultWithStatus<ShortLinkDto> createShortLink(@RequestBody ShortLinkRequestDto shortLinkRequestDto) {
        return shortLinkServiceFacade.createShortLink(shortLinkRequestDto);
    }


    @PutMapping("/update")
    public ResultWithStatus<ShortLinkDto> updateShortLink(@RequestBody ShortLinkUpdateRequestDto shortLinkRequestDto) {
        return shortLinkServiceFacade.updateShortLink(shortLinkRequestDto);
    }

    @DeleteMapping("/delete/{hash}")
    public ResultWithStatus<?> deleteByHash(@PathVariable String hash) {
        return shortLinkServiceFacade.deleteByHash(hash);
    }

}
