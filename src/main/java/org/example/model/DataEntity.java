package org.example.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "data_seq")
    private long id;
    private Instant time;
    private String hash;
    @ManyToOne
    @JsonIgnore
    private User user;
    private boolean isFound;
    private long lag;
}

