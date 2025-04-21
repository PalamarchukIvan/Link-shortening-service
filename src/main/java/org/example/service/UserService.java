package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.model.Role;
import org.example.model.User;
import org.example.model.VerificationToken;
import org.example.repository.TokenVerificationRepository;
import org.example.repository.UserRepository;
import org.example.util.CurrentUserUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static java.time.temporal.ChronoUnit.HOURS;

@Service
@RequiredArgsConstructor
public class UserService {

    @Value("${front-end-url}")
    private String domain;

    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final JavaMailSender mailSender;
    private final TokenVerificationRepository tokenVerificationRepository;

    public Optional<User> findActiveByUsername(String username) {
        return repository.findUserByUsernameAndIsActiveIsTrueAndIsVerifiedIsTrue(username);
    }

    public User createUser(User user) {
        Optional<User> possibleUserInDB = repository.findUserByUsername(user.getUsername());
        if (possibleUserInDB.isPresent()) {
            throw new IllegalStateException("User with such email already exists");
        }

        user.setRole(Collections.singletonList(Role.USER));
        user.setIsActive(Boolean.TRUE);
        user.setIsVerified(Boolean.FALSE);
        user.setPassword(encoder.encode(user.getPassword()));
        user = repository.save(user);

        String token = UUID.randomUUID().toString();
        Instant expiry = Instant.now().plus(24, HOURS);
        tokenVerificationRepository.save(
                VerificationToken
                .builder()
                        .token(token)
                        .expiry(expiry)
                        .user(user)
                .build()
        );

        String link = domain + "/verify?token=" + token;
        sendVerificationEmail(user.getUsername(), link);

        return user;
    }

    private void sendVerificationEmail(String to, String verifyLink) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject("Please verify your email");
        msg.setText("Click here to confirm your account:\n" + verifyLink);
        mailSender.send(msg);
    }

    public User updateUser(User newUser) {
        User oldUser = repository.findUserByUsernameAndIsActiveIsTrueAndIsVerifiedIsTrue(CurrentUserUtil.getCurrentUser().getUsername()).get();
        oldUser.setName(newUser.getName());
        oldUser.setRole(newUser.getRole());
        return repository.save(oldUser);
    }
}
