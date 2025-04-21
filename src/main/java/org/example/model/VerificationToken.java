package org.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "verification_token")
@SequenceGenerator(name = "token_seq", sequenceName = "token_seq", allocationSize = 1)
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "token_seq")
    private Long id;

    private String token;

    private Instant expiry;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

}
