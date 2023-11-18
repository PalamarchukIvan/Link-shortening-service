package org.example.web.restControllers;

import lombok.AllArgsConstructor;
import org.example.model.ShortLink;
import org.example.model.User;
import org.example.model.dto.ShortLinkRequestDto;
import org.example.model.dto.ShortLinkResponseDto;
import org.example.service.ShortLinkService;
import org.example.service.UserService;
import org.example.util.Mapstruct.ShortLinkMapper;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
@AllArgsConstructor
@RequestMapping(value = "/rest/short-links", produces = MediaType.APPLICATION_JSON_VALUE)
//@PreAuthorize("hasRole('ADMIN')")
public class ShortLinkRestController {
    private final UserService userService;
    private final ShortLinkService shortLinkService;
    private final ShortLinkMapper mapper;

    @GetMapping("/")
    public List<?> getShortLinksByUser() {
        User user = userService.findByUsername("user").orElseThrow();
        user.getLinks().removeIf(ShortLink::isDeleted);
        return user.getLinks();
    }

    @GetMapping("/create-short-link")
    public ShortLinkResponseDto createShortLink(@RequestBody ShortLinkRequestDto shortLinkRequestDto) {
        return mapper.toResponse(shortLinkService.create(mapper.fromRequest(shortLinkRequestDto)));
    }
}
