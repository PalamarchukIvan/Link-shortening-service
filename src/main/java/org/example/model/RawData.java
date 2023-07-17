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
    @EmbeddedId
    private RawDataKey key;
    private String hash;
    @Column(columnDefinition = "INTERVAL")
    private String expectedDuration;
}

