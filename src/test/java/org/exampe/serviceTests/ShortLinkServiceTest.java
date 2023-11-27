package org.exampe.serviceTests;

import org.example.model.DataEntity;
import org.example.model.ShortLink;
import org.example.model.User;
import org.example.repository.DataRepository;
import org.example.repository.ShortLinkRepository;
import org.example.service.ShortLinkService;
import org.example.util.CurrentUserUtil;
import org.example.util.exceptions.ResourceDeletedException;
import org.example.util.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShortLinkServiceTest {

    @Mock
    private ShortLinkRepository shortLinkRepository;

    @InjectMocks
    private ShortLinkService shortLinkService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateShortLink() {
        // Mock data
        User currentUser = User.builder().username("username").build();
        ShortLink shortLink = ShortLink.builder().link("123").hash("hash").user(currentUser).build();

        when(shortLinkRepository.save(any())).thenReturn(shortLink);

        ShortLink createdLink = shortLinkService.create(shortLink);

        // Assert the result
        assertNotNull(createdLink);
        assertEquals(currentUser, createdLink.getUser());
    }


    @Test
    void testGetByHashShouldThrowResourceNotFoundException() {
        // Mock data
        String hash = "nonExistentHash";

        when(shortLinkRepository.findById(hash)).thenReturn(java.util.Optional.empty());

        // Assert that ResourceNotFoundException is thrown
        assertThrows(ResourceNotFoundException.class, () -> shortLinkService.getByHash(hash));
    }

    @Test
    void testDeleteByHash() {
        // Mock data
        String hash = "existingHash";
        User currentUser = User.builder().username("username").build();
        ShortLink shortLink = ShortLink.builder().link("123").hash("hash").user(currentUser).build();

        when(shortLinkRepository.findById(hash)).thenReturn(java.util.Optional.of(shortLink));
        when(shortLinkRepository.save(any())).thenReturn(shortLink);

        ShortLink deletedLink = shortLinkService.deleteByHash(hash);

        // Assert the result
        assertNotNull(deletedLink);
        assertTrue(deletedLink.isDeleted());
    }
}
