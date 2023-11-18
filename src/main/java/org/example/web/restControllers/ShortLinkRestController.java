package org.example.web.restControllers;

import lombok.AllArgsConstructor;
import org.example.model.ShortLink;
import org.example.model.User;
import org.example.service.UserService;
import org.example.util.CurrentUserUtil;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
@AllArgsConstructor
@RequestMapping(value = "/rest/short-links", produces = MediaType.APPLICATION_JSON_VALUE)
//@PreAuthorize("hasRole('ADMIN')")
public class ShortLinkRestController {
    private final UserService userService;
    @GetMapping("/")
    public List<?> getShortLinksByUser() {
        User user = userService.findByUsername("user").orElseThrow();
        user.getLinks().removeIf(ShortLink::isDeleted);
        return user.getLinks();
    }
}
