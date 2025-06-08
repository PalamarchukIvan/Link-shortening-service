package org.exampe.serviceTests;

import org.example.dto.GetStatisticsDto;
import org.example.model.DataEntity;
import org.example.repository.DataRepository;
import org.example.service.DataService;
import org.example.util.exceptions.HashNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
        when(dataRepository.findAll(any(Specification.class), any(Sort.class))).thenReturn(List.of(
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
        List<DataEntity> actual = dataService.getFiltered(GetStatisticsDto.EMPTY);

        //then
        assertEquals(actual.size(), 4);
    }

    @Test
    void getAllWithFilterAmountTest() {
        // given
        when(dataRepository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(new PageImpl(List.of(
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
        )));

        // when
        List<DataEntity> actual = dataService.getFiltered(GetStatisticsDto
                .builder()
                .user(currentUser)
                .amount(5)
                .build()
        );

        //then
        assertEquals(actual.size(), 4);
    }

    @Test
    void getAllWithFilterUserTest() {
        // given
        when(dataRepository.findAll(any(Specification.class), any(Sort.class))).thenReturn(List.of(
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
        List<DataEntity> actual = dataService.getFiltered(GetStatisticsDto
                .builder()
                .user(currentUser)
                .build()
        );
        //then
        assertEquals(actual.size(), 4);
    }

    @Test
    void getAllWithFilterDateTest() {
        // given
        when(dataRepository.findAll(any(Specification.class), any(Sort.class))).thenReturn(List.of(
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
        List<DataEntity> actual = dataService.getFiltered(GetStatisticsDto
                .builder()
                .user(currentUser)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.ofEpochSecond(Instant.now().toEpochMilli() + 1000L, 0, ZoneOffset.UTC))
                .build()
        );
        //then
        assertEquals(actual.size(), 4);
    }

    @Test
    void getAllWithFilterHashTest() {
        // given
        when(dataRepository.findAll(any(Specification.class), any(Sort.class))).thenReturn(List.of(
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
        List<DataEntity> actual = dataService.getFiltered(GetStatisticsDto
                .builder()
                .user(currentUser)
                .hash("123")
                .build()
        );

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
