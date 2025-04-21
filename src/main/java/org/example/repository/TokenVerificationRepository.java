package org.example.repository;

import org.example.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenVerificationRepository extends JpaRepository<VerificationToken, Long> {

    VerificationToken findByToken(String token);

}
