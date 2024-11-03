package org.example.specification;

import jakarta.persistence.criteria.*;
import org.example.model.DataEntity;
import org.example.model.User;
import org.example.model.dto.DataFilterRequestDto;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataRepositorySpecificationBuilder {
    public static Specification<DataEntity> prepareSpecification(DataFilterRequestDto filter) {
        return (Root<DataEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            query.orderBy(cb.desc(root.get("time")));

            if (filter.getUserId() != null) {
                Join<DataEntity, User> userJoin = root.join("user");
                predicates.add(cb.equal(userJoin.get("id"), filter.getUserId()));
            }

            if (filter.getHash() != null) {
                predicates.add(cb.equal(root.get("hash"), filter.getHash()));
            }

            return predicates.isEmpty() ? cb.conjunction() : cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
