package org.example.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "ShortLinks", indexes = @Index(name = "idx_hashcode", columnList = "hash"))
public class ShortLink {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String hash;
    private String link;
    private boolean isDeleted;
}
