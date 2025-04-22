package org.example.web.restControllers;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.example.dto.LoginRequestDto;
import org.example.model.User;
import org.example.repository.TokenVerificationRepository;
import org.example.facade.UserControllerFacade;
import org.example.service.UserService;
import org.example.util.security.JwtService;
import org.example.util.web.ResponseStatusFromResult;
import org.example.web.ResultWithStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@AllArgsConstructor
@RequestMapping("/rest/user")
@CrossOrigin(originPatterns = "http://localhost:3000", allowCredentials = "true")
public class UserRestController {

    private final UserControllerFacade userControllerFacade;

    private final UserService userService;
    private final JwtService jwtService;
    private final TokenVerificationRepository tokenRepo;

    @PostMapping("/login")
    @ResponseStatusFromResult
    public ResultWithStatus<User> doLogin(
            @RequestBody LoginRequestDto login,
            HttpServletResponse response
    ) {
        var result = userService.doLogin(login.getUsername(), login.getPassword());
        if (result.getStatus().is2xxSuccessful()) {
            String token = jwtService.generateToken(login.getUsername());

            // build a SameSite=None cookie
            ResponseCookie cookie = ResponseCookie.from("AUTH_TOKEN", token)
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .maxAge(jwtService.getExpirationMs() / 1000)
                    .sameSite("Strict")
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        }
        return result;
    }

    @PostMapping("/registration")
    @ResponseStatusFromResult
    public ResultWithStatus<User> doRegistration(
            @RequestBody User user,
            HttpServletResponse response
    ) {
        try {
            var created = userService.createUser(user);
            String token = jwtService.generateToken(created.getUsername());
            ResponseCookie cookie = ResponseCookie.from("AUTH_TOKEN", token)
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .maxAge(jwtService.getExpirationMs() / 1000)
                    .sameSite("Strict")
                    .build();
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
            return ResultWithStatus.ok(created);
        } catch (Exception e) {
            return ResultWithStatus.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("/verify")
    @ResponseStatusFromResult
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
