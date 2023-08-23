package org.example.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnalyzedData {
    @Id
    @Column(nullable = false, columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Instant time;
    private String hash;
    private Long expectedDuration;
    private boolean isFound;
    private long lag;
}

