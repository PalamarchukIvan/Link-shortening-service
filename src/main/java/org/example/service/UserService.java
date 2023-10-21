package org.example.service;

import lombok.AllArgsConstructor;
import org.example.model.Role;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final PasswordEncoder encoder;
    public Optional<User> findByUsername(String username) {
        return repository.findUserByUsername(username);
    }
    public User createUser(User user) {
        user.setRole(Collections.singletonList(Role.USER));
        user.setIsActive(Boolean.TRUE);
        user.setPassword(encoder.encode(user.getPassword()));
        return repository.save(user);
    }
}
