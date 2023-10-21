package org.example.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "data")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataEntity {
    @Id
    @Column(nullable = false, columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Instant time;
    private String hash;
    @ManyToOne
    private User user;
    private long expectedDuration;
    private boolean isFound;
    private long lag;
}
