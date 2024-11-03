package org.exampe.serviceTests;

import org.example.model.Role;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.example.service.UserService;
import org.example.util.CurrentUserUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest extends FunctionalTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByUsername() {
        String username = "testUser";

        when(userRepository.findUserByUsername(username)).thenReturn(Optional.of(currentUser));

        Optional<User> foundUser = userService.findByUsername(username);

        assertTrue(foundUser.isPresent());
        assertEquals(currentUser, foundUser.get());
    }


    @Test
    void testCreateUser() {
        when(passwordEncoder.encode(currentUser.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any())).thenReturn(currentUser);

        User createdUser = userService.createUser(currentUser);

        assertNotNull(createdUser);
        assertEquals(Collections.singletonList(Role.USER), createdUser.getRole());
        assertTrue(createdUser.getIsActive());
        assertEquals("encodedPassword", createdUser.getPassword());
    }

    @Test
    void testUpdateUser() {
        User newUser = User.builder()
                .username("new-name")
                .password("new-pass")
                .isActive(false)
                .build();
        User oldUser = User.builder()
                .username("name")
                .password("pass")
                .isActive(true)
                .build();

        when(userRepository.findUserByUsername(oldUser.getUsername())).thenReturn(Optional.of(oldUser));
        when(userRepository.save(any())).thenReturn(oldUser);

        User updatedUser = userService.updateUser(newUser);

        assertNotNull(updatedUser);
        assertEquals(newUser.getName(), updatedUser.getName());
    }
}