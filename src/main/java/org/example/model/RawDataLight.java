package org.example.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "raw_data_light", indexes = @Index(name = "idx_hashcode_raw_data", columnList = "hash"))
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RawDataLight {
    @Id
    @Column(nullable = false, columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Instant time;
    private String hash;
    private boolean isFound;
    private long lag;
    private String prevHash;
    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Instant prevTime; //для корректного форматирования последней записи в таблице при выводе ее
}

