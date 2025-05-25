package org.example.service;

import lombok.AllArgsConstructor;
import org.example.facade.UserControllerFacade;
import org.example.model.User;
import org.example.model.VerificationToken;
import org.example.repository.TokenVerificationRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.HOURS;

@Service
@AllArgsConstructor
public class TokenVerificationService {
    private final TokenVerificationRepository tokenVerificationRepository;

    public void createToken(String token, User user) {
        Instant expiry = Instant.now().plus(24, HOURS);
        tokenVerificationRepository.save(
                VerificationToken
                        .builder()
                        .token(token)
                        .expiry(expiry)
                        .user(user)
                        .build()
        );
    }

    public Optional<VerificationToken> findByToken(String token) {
        return tokenVerificationRepository.findByToken(token);
    }

    public void delete(VerificationToken token) {
        tokenVerificationRepository.delete(token);
    }

}
