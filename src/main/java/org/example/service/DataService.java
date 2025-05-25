package org.example.service;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.example.dto.GetStatisticsDto;
import org.example.model.DataEntity;
import org.example.model.User;
import org.example.repository.DataRepository;
import org.example.repository.util.DataSpecifications;
import org.example.util.exceptions.HashNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class DataService {
    private final DataRepository repository;

    public List<DataEntity> getFiltered(GetStatisticsDto request) {
        User user = request.getUser();
        String hash = request.getHash();
        LocalDateTime startDate = request.getStartDate();
        LocalDateTime endDate = request.getEndDate();
        Integer amount = request.getAmount();

        // build spec
        Specification<DataEntity> spec = DataSpecifications.build(
                user != null ? user.getId() : null,
                hash,
                startDate != null ? startDate.toInstant(ZoneOffset.UTC) : null,
                endDate   != null ? endDate.toInstant(ZoneOffset.UTC)   : null
        );

        // choose paging or simple sort
        List<DataEntity> list;
        if (amount != null && amount > 0) {
            // fetch the *latest* 'amount' records by time DESC, then reverse them
            PageRequest page = PageRequest.of(
                    0,
                    amount,
                    Sort.by(Sort.Direction.DESC, "time")
            );
            list = new ArrayList<>(repository.findAll(spec, page).getContent());
            Collections.reverse(list);
        } else {
            list = repository.findAll(spec, Sort.by("time"));
        }

        return list;
    }
}
