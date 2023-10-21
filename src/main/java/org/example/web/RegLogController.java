package org.example.web;

import lombok.AllArgsConstructor;
import org.example.model.Role;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.example.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;
import java.util.Map;

@Controller
@AllArgsConstructor
public class RegLogController {
    private final UserService service;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String main() {
        return "main";
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
