package org.example.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "raw_data")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RawData {
    @Id
    @Column(nullable = false, columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Instant time;
    private String hash;
    private long expectedDuration;
    private boolean isFound;
    private long lag;
}

