package org.example.web.restControllers;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.example.dto.LoginRequestDto;
import org.example.facade.UserControllerFacade;
import org.example.model.User;
import org.example.util.web.ResponseStatusFromResult;
import org.example.web.ResultWithStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/rest/user")
@CrossOrigin(originPatterns = "http://localhost:3000", allowCredentials = "true")
public class UserRestController {

    private final UserControllerFacade userControllerFacade;

    @PostMapping("/login")
    @ResponseStatusFromResult
    public ResultWithStatus<User> doLogin(
            @RequestBody LoginRequestDto login,
            HttpServletResponse response
    ) {
        return userControllerFacade.doLogin(login, response);
    }

    @PostMapping("/registration")
    @ResponseStatusFromResult
    public ResultWithStatus<User> doRegistration(
            @RequestBody User user,
            HttpServletResponse response
    ) {
        return userControllerFacade.doRegistration(user, response);
    }

    @PostMapping("/verify")
    @ResponseStatusFromResult
    public ResultWithStatus<?> verify(@RequestParam String token) {
        return userControllerFacade.verify(token);
    }

    @PatchMapping("/update")
    @ResponseStatusFromResult
    public ResultWithStatus<User> editProfile(@RequestBody User user) {
        return userControllerFacade.editProfile(user);
    }

    @GetMapping("/current")
    @ResponseStatusFromResult
    public ResultWithStatus<User> getCurrentUser() {
        return userControllerFacade.getCurrentUser();
    }

    @GetMapping("/profile")
    @ResponseStatusFromResult
    public ResultWithStatus<User> getCurrentUser(@RequestParam String login) {
        return userControllerFacade.getUser(login);
    }
}
