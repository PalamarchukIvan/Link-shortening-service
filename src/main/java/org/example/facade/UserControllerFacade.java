package org.example.facade;

import lombok.RequiredArgsConstructor;
import org.example.model.User;
import org.example.repository.TokenVerificationRepository;
import org.example.service.UserService;
import org.example.util.CurrentUserUtil;
import org.example.util.security.JwtService;
import org.example.web.ResultWithStatus;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserControllerFacade {

    private final UserService userService;

    public ResultWithStatus<User> getCurrentUser() {
        Optional<User> user = userService.findActiveByUsername(CurrentUserUtil.getCurrentUser().getUsername());
        if (!user.isPresent()) {
            return ResultWithStatus.error(HttpStatus.BAD_REQUEST, "User is not found");
        }
        return ResultWithStatus.ok(user.get());
    }

    public ResultWithStatus<User> editProfile(User user) {
        return ResultWithStatus.ok(userService.updateCurrentUser(user));
    }
}
