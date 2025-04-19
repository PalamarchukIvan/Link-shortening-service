package org.example.repository;

import org.example.model.DataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataRepository extends JpaRepository<DataEntity, Long>, JpaSpecificationExecutor<DataEntity> {
    @Query(nativeQuery = true, value =  " select * from " +
            "   (select * from data where user_id = :userId order by time desc limit :amount) as tb" +
            " order by time")
    List<DataEntity> findLast(int amount, long userId);
}
