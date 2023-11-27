package org.exampe.serviceTests;

import org.example.model.DataEntity;
import org.example.model.User;
import org.example.repository.DataRepository;
import org.example.service.DataService;
import org.example.util.exceptions.HashNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DataServiceTest {

    @Mock
    private DataRepository dataRepository;

    @InjectMocks
    private DataService dataService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAll() {
        // Mock data from repository
        when(dataRepository.findAll()).thenReturn(Arrays.asList(
                DataEntity.builder().build(),
                DataEntity.builder().build()
        ));

        List<DataEntity> result = dataService.getAll();

        // Assert the result
        assertNotNull(result);
        assertEquals(2, result.size());
        // Add more assertions based on your specific requirements
    }

    // Add more test cases for other methods in DataService class...

    @Test
    void testGetAllWithHashShouldThrowHashNotFoundException() {
        // Mock data from repository
        when(dataRepository.findAllByHash(anyString())).thenReturn(Arrays.asList());

        // Assert that HashNotFoundException is thrown
        assertThrows(HashNotFoundException.class, () -> dataService.getAllWithHash("someHash"));
    }

    @Test
    void testGetAllWithFilter() {
        User user = User.builder().username("test").build();
        // Mock data from repository
        when(dataRepository.findAll()).thenReturn(Arrays.asList(
                DataEntity.builder().user(user).hash("someHash").build(),
                DataEntity.builder().user(user).build()
        ));

        String hash = "someHash";

        List<DataEntity> result = dataService.getAllWithFilter(2, user, null, null, hash);

        // Assert the result
        assertNotNull(result);
        assertEquals(1, result.size());
        // Add more assertions based on your specific requirements
    }

}
