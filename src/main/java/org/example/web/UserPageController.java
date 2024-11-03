package org.example.web;

import lombok.RequiredArgsConstructor;
import org.example.model.ShortLink;
import org.example.model.User;
import org.example.service.UserService;
import org.example.util.CurrentUserUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class UserPageController {
    private final UserService userService;

    @Deprecated
    @GetMapping("/main")
    public String main(Model model) {
        User user = userService.findByUsername(CurrentUserUtil.getCurrentUser().getUsername()).orElseThrow();
        user.getLinks().removeIf(ShortLink::isDeleted);
        model.addAttribute("user", user);
        return "main";
    }
}
