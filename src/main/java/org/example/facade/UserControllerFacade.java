package org.example.facade;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.dto.LoginRequestDto;
import org.example.model.User;
import org.example.service.TokenVerificationService;
import org.example.service.UserService;
import org.example.util.CurrentUserUtil;
import org.example.util.security.JwtService;
import org.example.web.ResultWithStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserControllerFacade {

    private final UserService userService;
    private final JwtService jwtService;
    private final TokenVerificationService tokenVerificationService;

    public ResultWithStatus<User> getCurrentUser() {
        Optional<User> user = userService.findActiveByUsername(CurrentUserUtil.getCurrentUser().getUsername());
        if (!user.isPresent()) {
            return ResultWithStatus.error(HttpStatus.BAD_REQUEST, "User is not found");
        }
        return ResultWithStatus.ok(user.get());
    }

    public ResultWithStatus<User> getUser(String login) {
        Optional<User> user = userService.findActiveByUsername(login);
        if (!user.isPresent()) {
            return ResultWithStatus.error(HttpStatus.BAD_REQUEST, "User is not found");
        }
        return ResultWithStatus.ok(user.get());
    }

    public ResultWithStatus<User> editProfile(User user) {
        return ResultWithStatus.ok(userService.updateCurrentUser(user));
    }

    public ResultWithStatus<User> doLogin(
            LoginRequestDto login,
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

    public ResultWithStatus<User> doRegistration(
            User user,
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

    public ResultWithStatus<?> verify(String token) {
        return tokenVerificationService.findByToken(token)
                .filter(t -> t.getExpiry().isAfter(Instant.now()))
                .map(t -> {
                    User u = t.getUser();
                    userService.setUserVerified(u.getUsername());
                    tokenVerificationService.delete(t);
                    return ResultWithStatus.ok();
                })
                .orElseGet(() -> ResultWithStatus
                        .error(HttpStatus.BAD_REQUEST, "Invalid or expired token."));
    }
}
