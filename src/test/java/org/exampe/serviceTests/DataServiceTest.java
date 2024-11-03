package org.exampe.serviceTests;

import org.example.model.DataEntity;
import org.example.repository.DataRepository;
import org.example.service.DataService;
import org.example.util.exceptions.HashNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

class DataServiceTest extends FunctionalTest {

    @Mock
    private DataRepository dataRepository;

    @InjectMocks
    private DataService dataService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllTest() {
        // given
        when(dataRepository.findAll()).thenReturn(List.of(
                DataEntity.builder()
                        .hash("123")
                        .time(Instant.now())
                        .isFound(true)
                        .user(currentUser)
                        .build(),
                DataEntity.builder()
                        .hash("123")
                        .time(Instant.now())
                        .isFound(true)
                        .user(currentUser)
                        .build(),
                DataEntity.builder()
                        .hash("123")
                        .time(Instant.now())
                        .isFound(true)
                        .user(currentUser)
                        .build(),
                DataEntity.builder()
                        .hash("123")
                        .time(Instant.now())
                        .isFound(true)
                        .user(currentUser)
                        .build()
        ));

        // when
        List<DataEntity> actual = dataService.getAll();

        //then
        assertEquals(actual.size(), 4);
    }

    @Test
    void testGetAllWithHashShouldThrowHashNotFoundException() {
        when(dataRepository.findAllByHash(anyString())).thenReturn(Arrays.asList());
        assertThrows(HashNotFoundException.class, () -> dataService.getAllWithHash("someHash"));
    }

    @Test
    void getAllWithFilterAmountTest() {
        // given
        when(dataRepository.findAll()).thenReturn(List.of(
                DataEntity.builder()
                        .hash("123")
                        .time(Instant.now())
                        .isFound(true)
                        .user(currentUser)
                        .build(),
                DataEntity.builder()
                        .hash("123")
                        .time(Instant.now())
                        .isFound(true)
                        .user(currentUser)
                        .build(),
                DataEntity.builder()
                        .hash("123")
                        .time(Instant.now())
                        .isFound(true)
                        .user(currentUser)
                        .build(),
                DataEntity.builder()
                        .hash("123")
                        .time(Instant.now())
                        .isFound(true)
                        .user(currentUser)
                        .build()
        ));

        // when
        List<DataEntity> actual = dataService.getAllWithFilter(5, currentUser, null, null,null);

        //then
        assertEquals(actual.size(), 4);
    }
    @Test
    void getAllWithFilterUserTest() {
        // given
        when(dataRepository.findAll()).thenReturn(List.of(
                DataEntity.builder()
                        .hash("123")
                        .time(Instant.now())
                        .isFound(true)
                        .user(currentUser)
                        .build(),
                DataEntity.builder()
                        .hash("123")
                        .time(Instant.now())
                        .isFound(true)
                        .user(currentUser)
                        .build(),
                DataEntity.builder()
                        .hash("123")
                        .time(Instant.now())
                        .isFound(true)
                        .user(currentUser)
                        .build(),
                DataEntity.builder()
                        .hash("123")
                        .time(Instant.now())
                        .isFound(true)
                        .user(currentUser)
                        .build()
        ));

        // when
        List<DataEntity> actual = dataService.getAllWithFilter(null, currentUser, null, null,null);

        //then
        assertEquals(actual.size(), 4);
    }
    @Test
    void getAllWithFilterDateTestNoResult() {
        // given
        when(dataRepository.findAll()).thenReturn(List.of(
                DataEntity.builder()
                        .hash("123")
                        .time(Instant.now())
                        .isFound(true)
                        .user(currentUser)
                        .build(),
                DataEntity.builder()
                        .hash("123")
                        .time(Instant.now())
                        .isFound(true)
                        .user(currentUser)
                        .build(),
                DataEntity.builder()
                        .hash("123")
                        .time(Instant.now())
                        .isFound(true)
                        .user(currentUser)
                        .build(),
                DataEntity.builder()
                        .hash("123")
                        .time(Instant.now())
                        .isFound(true)
                        .user(currentUser)
                        .build()
        ));

        // when
        List<DataEntity> actual = dataService.getAllWithFilter(null, currentUser, new Date(), new Date(),null);

        //then
        assertEquals(actual.size(), 0);
    }
    @Test
    void getAllWithFilterDateTest() {
        // given
        when(dataRepository.findAll()).thenReturn(List.of(
                DataEntity.builder()
                        .hash("123")
                        .time(Instant.now())
                        .isFound(true)
                        .user(currentUser)
                        .build(),
                DataEntity.builder()
                        .hash("123")
                        .time(Instant.now())
                        .isFound(true)
                        .user(currentUser)
                        .build(),
                DataEntity.builder()
                        .hash("123")
                        .time(Instant.now())
                        .isFound(true)
                        .user(currentUser)
                        .build(),
                DataEntity.builder()
                        .hash("123")
                        .time(Instant.now())
                        .isFound(true)
                        .user(currentUser)
                        .build()
        ));

        // when
        List<DataEntity> actual = dataService.getAllWithFilter(null, currentUser, new Date(0), new Date(Instant.now().toEpochMilli() + 1000L),null);

        //then
        assertEquals(actual.size(), 4);
    }
    @Test
    void getAllWithFilterHashTest() {
        // given
        when(dataRepository.findAll()).thenReturn(List.of(
                DataEntity.builder()
                        .hash("123")
                        .time(Instant.now())
                        .isFound(true)
                        .user(currentUser)
                        .build(),
                DataEntity.builder()
                        .hash("123")
                        .time(Instant.now())
                        .isFound(true)
                        .user(currentUser)
                        .build(),
                DataEntity.builder()
                        .hash("123")
                        .time(Instant.now())
                        .isFound(true)
                        .user(currentUser)
                        .build(),
                DataEntity.builder()
                        .hash("123")
                        .time(Instant.now())
                        .isFound(true)
                        .user(currentUser)
                        .build()
        ));

        // when
        List<DataEntity> actual = dataService.getAllWithFilter(null, currentUser, null,null, "123");

        //then
        assertEquals(actual.size(), 4);
    }

    @Test
    void getAllWithLimitTest() {

    }

    @Test
    void getAllByUserTest() {

    }

}
