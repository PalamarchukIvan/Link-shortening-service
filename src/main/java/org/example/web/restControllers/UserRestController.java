package org.example.web.restControllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.example.facade.UserControllerFacade;
import org.example.model.User;
import org.example.repository.TokenVerificationRepository;
import org.example.service.UserService;
import org.example.util.security.JwtService;
import org.example.web.ResultWithStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@AllArgsConstructor
@RequestMapping("/rest/user")
public class UserRestController {

    private final UserControllerFacade userControllerFacade;

    private final UserService userService;
    private final JwtService jwtService;
    private final TokenVerificationRepository tokenRepo;

    @PostMapping("/login")
    public ResultWithStatus<User> doLogin(
            @RequestParam String username,
            @RequestParam String password,
            HttpServletResponse response
    ) {
        var result = userService.doLogin(username, password);
        if (result.getStatus() == HttpStatus.OK) {
            String token = jwtService.generateToken(username);

            Cookie cookie = new Cookie("AUTH_TOKEN", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge((int) (jwtService.getExpirationMs() / 1000));
            response.addCookie(cookie);
        }
        return result;
    }

    @PostMapping("/registration")
    public ResultWithStatus<User> doRegistration(
            @RequestBody User user,
            HttpServletResponse response
    ) {
        var created = userService.createUser(user);
        String token = jwtService.generateToken(created.getUsername());
        Cookie cookie = new Cookie("AUTH_TOKEN", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge((int) (jwtService.getExpirationMs() / 1000));
        response.addCookie(cookie);

        return ResultWithStatus.ok(created);
    }

    @PostMapping("/verify")
    public ResultWithStatus<?> verify(@RequestParam String token) {
        return tokenRepo.findByToken(token)
                .filter(t -> t.getExpiry().isAfter(Instant.now()))
                .map(t -> {
                    User u = t.getUser();
                    userService.setUserVerified(u.getUsername());
                    tokenRepo.delete(t);
                    return ResultWithStatus.ok();
                })
                .orElseGet(() -> ResultWithStatus
                        .error(HttpStatus.BAD_REQUEST, "Invalid or expired token."));
    }

    @PatchMapping("/update")
    public ResultWithStatus<User> editProfile(@RequestBody User user) {
        return userControllerFacade.editProfile(user);
    }

    @GetMapping("/current")
    public ResultWithStatus<User> getCurrentUser() {
        return userControllerFacade.getCurrentUser();
    }
}
