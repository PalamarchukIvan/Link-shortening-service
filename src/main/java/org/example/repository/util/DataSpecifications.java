package org.example.repository.util;

import jakarta.annotation.Nullable;
import org.example.model.DataEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.Instant;

public class DataSpecifications {

    public static Specification<DataEntity> belongsToUser(Long userId) {
        return (root, query, cb) ->
                cb.equal(root.get("user").get("id"), userId);
    }

    public static Specification<DataEntity> withHash(String hash) {
        return (root, query, cb) ->
                cb.equal(root.get("hash"), hash);
    }

    public static Specification<DataEntity> timeAfter(Instant start) {
        return (root, query, cb) ->
                cb.greaterThanOrEqualTo(root.get("time"), start);
    }

    public static Specification<DataEntity> timeBefore(Instant end) {
        return (root, query, cb) ->
                cb.lessThanOrEqualTo(root.get("time"), end);
    }

    public static Specification<DataEntity> build(
            @Nullable Long    userId,
            @Nullable String  hash,
            @Nullable Instant start,
            @Nullable Instant end
    ) {
        // start with "always TRUE"
        Specification<DataEntity> spec = (root, query, cb) -> cb.conjunction();

        if (userId != null) {
            spec = spec.and(belongsToUser(userId));
        }
        if (StringUtils.hasText(hash)) {
            spec = spec.and(withHash(hash));
        }
        if (start != null) {
            spec = spec.and(timeAfter(start));
        }
        if (end != null) {
            spec = spec.and(timeBefore(end));
        }

        return spec;
    }
}
