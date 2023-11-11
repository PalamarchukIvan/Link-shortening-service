package org.example.repository;

import org.example.model.DataEntity;
import org.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.util.List;

@Repository
public interface DataRepository extends JpaRepository<DataEntity, Long> {
    @Query(nativeQuery = true, value =  "select * from data rd where rd.user_id = :userId")
    List<DataEntity> findAllByUser(Long userId);

    @Query(nativeQuery = true, value =  "select * from data rd where rd.hash = :hash")
    List<DataEntity> findAllByHash(String hash);
    @Query(nativeQuery = true, value =  "select * from data rd where rd.hash = :hash and user_id = :userId")
    List<DataEntity> findAllByHash(String hash, long userId);
    @Query(nativeQuery = true, value =  " select * from " +
                                        "   (select * from data where hash = :hash order by time desc limit :amount) as tb" +
                                        " order by time")
    List<DataEntity> findLastByHash(String hash, int amount);
    @Query(nativeQuery = true, value =  " select * from " +
                                        "   (select * from data where hash = :hash and user_id = :userId order by time desc limit :amount) as tb" +
                                        " order by time")
    List<DataEntity> findLastByHash(String hash, int amount, long userId);
    @Query(nativeQuery = true, value =  " select * from " +
                                        "   (select * from data order by time desc limit :amount) as tb" +
                                        " order by time")
    List<DataEntity> findLast(int amount);
    @Query(nativeQuery = true, value =  " select * from " +
                                        "   (select * from data where user_id = :userId order by time desc limit :amount) as tb" +
                                        " order by time")
    List<DataEntity> findLast(int amount, long userId);
}
