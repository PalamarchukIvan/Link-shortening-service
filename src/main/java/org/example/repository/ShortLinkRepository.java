package org.example.repository;

import org.example.model.ShortLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ShortLinkRepository extends JpaRepository<ShortLink, String> {
    @Query(nativeQuery = true, value = "select count(*) from short_links where hash = :hash")
    short checkIfUniqueHash(String hash);
}
