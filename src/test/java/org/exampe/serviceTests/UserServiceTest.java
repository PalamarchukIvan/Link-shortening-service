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

class UserServiceTest {

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
        // Mock data
        String username = "testUser";
        User user = new User(/* provide necessary data for User */);

        when(userRepository.findUserByUsername(username)).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.findByUsername(username);

        // Assert the result
        assertTrue(foundUser.isPresent());
        assertEquals(user, foundUser.get());
        // Add more assertions based on your specific requirements
    }


    @Test
    void testCreateUser() {
        // Mock data
        User user = new User(/* provide necessary data for User */);
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any())).thenReturn(user);

        User createdUser = userService.createUser(user);

        // Assert the result
        assertNotNull(createdUser);
        assertEquals(Collections.singletonList(Role.USER), createdUser.getRole());
        assertTrue(createdUser.getIsActive());
        assertEquals("encodedPassword", createdUser.getPassword());
        // Add more assertions based on your specific requirements
    }

    @Test
    void testUpdateUser() {
        // Mock data
        User newUser = new User(/* provide necessary data for User */);
        User oldUser = new User(/* provide necessary data for User */);

        when(userRepository.findUserByUsername(oldUser.getUsername())).thenReturn(Optional.of(oldUser));
        when(userRepository.save(any())).thenReturn(oldUser);

        User updatedUser = userService.updateUser(newUser);

        // Assert the result
        assertNotNull(updatedUser);
        assertEquals(newUser.getName(), updatedUser.getName());
    }
}