package org.example.web;

import lombok.AllArgsConstructor;
import org.example.model.User;
import org.example.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
@PreAuthorize("isAuthenticated()")
public class RegLogController {
    private final UserService service;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String greetings() {
        return "greetings";
    }

    @PostMapping("/login")
    public String doLogin(User user, Model model) {
        if(service.findByUsername((user.getUsername())).isEmpty()){
            model.addAttribute("error", new ErrorResponseDTO("User with such username does not exists"));
            return "login";
        }
        return "redirect:/";
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String doRegistration(User user, Model model) {
        if(service.findByUsername((user.getUsername())).isPresent()){
            model.addAttribute("error", new ErrorResponseDTO("Such user already exists"));
            return "registration";
        }
        service.createUser(user);

        return "redirect:/stat/statistic";
    }
}
