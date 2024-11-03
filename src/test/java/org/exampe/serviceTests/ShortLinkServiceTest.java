package org.exampe.serviceTests;

import org.example.model.ShortLink;
import org.example.model.User;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class ShortLinkServiceTest extends FunctionalTest {

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
        User currentUser = User.builder().username("username").build();
        ShortLink shortLink = ShortLink.builder().link("123").hash("hash").user(currentUser).build();
        SecurityContextHolder.setContext(new SecurityContextImpl(new UsernamePasswordAuthenticationToken(currentUser, null)));
        when(shortLinkRepository.save(any())).thenReturn(shortLink);

        ShortLink createdLink = shortLinkService.create(shortLink);

        assertNotNull(createdLink);
        assertEquals(currentUser, createdLink.getUser());
    }


    @Test
    void testGetByHashShouldThrowResourceNotFoundException() {
        String hash = "nonExistentHash";

        when(shortLinkRepository.findById(hash)).thenReturn(java.util.Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> shortLinkService.getByHash(hash));
    }

    @Test
    void testDeleteByHash() {
        String hash = "existingHash";
        User currentUser = User.builder().username("username").build();
        ShortLink shortLink = ShortLink.builder().link("123").hash("hash").user(currentUser).build();

        when(shortLinkRepository.findById(hash)).thenReturn(java.util.Optional.of(shortLink));
        when(shortLinkRepository.save(any())).thenReturn(shortLink);

        ShortLink deletedLink = shortLinkService.deleteByHash(hash);

        assertNotNull(deletedLink);
        assertTrue(deletedLink.isDeleted());
    }

    @Test
    void testGetByHash() {
        String hash = "existingHash";

        when(shortLinkRepository.findById(hash)).thenReturn(Optional.of(ShortLink.builder().build()));

        assertNotNull(shortLinkService.getByHash(hash));
    }

    @Test
    void testGetByHashIsDeleted() {
        String hash = "deletedHash";

        when(shortLinkRepository.findById(hash)).thenReturn(Optional.of(ShortLink.builder().isDeleted(true).build()));

        assertThrows(ResourceDeletedException.class, () -> shortLinkService.getByHash(hash));
    }

    @Test
    void testGetHashCodeIsUnique() {
        List<String> previousHashes = new ArrayList<>();

        when(shortLinkRepository.checkIfUniqueHash(any())).then((invocation)-> {
            if (previousHashes.contains(invocation.getArguments()[0])) {
                return (short) 1;
            } else {
                return (short) 0;
            }
        });

        for (int i = 0; i < 100000; i++) {
            String hashCode = shortLinkService.getHashCode();
            assertFalse(previousHashes.contains(hashCode));
            previousHashes.add(hashCode);
        }
    }

}
