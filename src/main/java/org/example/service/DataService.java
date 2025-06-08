package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.GetStatisticsDto;
import org.example.model.DataEntity;
import org.example.model.User;
import org.example.repository.DataRepository;
import org.example.repository.util.DataSpecifications;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

        Specification<DataEntity> spec = DataSpecifications.build(
                user != null ? user.getId() : null,
                hash,
                startDate != null ? startDate.toInstant(ZoneOffset.UTC) : null,
                endDate   != null ? endDate.toInstant(ZoneOffset.UTC)   : null
        );

        List<DataEntity> list;
        if (amount != null && amount > 0) {
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
