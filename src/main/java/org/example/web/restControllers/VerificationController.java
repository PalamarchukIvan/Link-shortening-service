package org.example.web.restControllers;

import lombok.RequiredArgsConstructor;
import org.example.model.User;
import org.example.repository.TokenVerificationRepository;
import org.example.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequiredArgsConstructor
public class VerificationController {
    private final TokenVerificationRepository tokenRepo;
    private final UserRepository userRepo;

    @GetMapping("/verify")
    public ResponseEntity<?> verify(@RequestParam String token) {
        return tokenRepo.findById(Long.valueOf(token))
                .filter(t -> t.getExpiry().isAfter(Instant.now()))
                .map(t -> {
                    User u = t.getUser();
                    u.setIsVerified(true);
                    userRepo.save(u);
                    tokenRepo.delete(t);
                    return ResponseEntity.ok("Email verified!");
                })
                .orElseGet(() -> ResponseEntity
                        .badRequest()
                        .body("Invalid or expired token."));
    }
}
