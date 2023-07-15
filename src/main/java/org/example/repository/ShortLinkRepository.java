package org.example.repository;

import org.example.model.ShortLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShortLinkRepository extends JpaRepository<ShortLink, Long> {
    @Query("select link from ShortLink link where link.hash = :hash")
    Optional<ShortLink> findByHash(String hash);

}
