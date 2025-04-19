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
        Date startDate = request.getStartDate();
        Date endDate = request.getEndDate();
        Integer amount = request.getAmount();

        // build spec
        Specification<DataEntity> spec = DataSpecifications.build(
                user != null ? user.getId() : null,
                hash,
                startDate != null ? startDate.toInstant() : null,
                endDate   != null ? endDate.toInstant()   : null
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

        // if you need to throw on no‑hash, you can do that before or after—
        // e.g. if (list.isEmpty() && hash!=null) throw new HashNotFoundException();
        if (list.isEmpty() && StringUtils.hasText(hash)) {
            throw new HashNotFoundException();
        }

        // post‑processing of durations
        return formatLastRecord(list);
    }

    private static List<DataEntity> formatLastRecord(List<DataEntity> result) {
        int size = result.size();
        if (size == 0) {
            return result;
        }

        DataEntity last  = result.get(size - 1);
        DataEntity prev  = size > 1 ? result.get(size - 2) : null;

        long calculatedDuration;
        Instant now = Instant.now();
        if (prev != null && Objects.equals(last.getHash(), prev.getHash())) {
            Instant adjustedStart = last.getTime()
                    .minusMillis(prev.getExpectedDuration());
            calculatedDuration = Duration.between(adjustedStart, now)
                    .toMillis();
        } else {
            calculatedDuration = Duration.between(last.getTime(), now)
                    .toMillis();
        }
        last.setExpectedDuration(calculatedDuration);
        return result;
    }
}
