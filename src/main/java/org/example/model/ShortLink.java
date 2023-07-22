package org.example.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "ShortLinks", indexes = @Index(name = "idx_hashcode", columnList = "hash"))
public class ShortLink {
    @Id
    private String hash;
    private String link;
    private boolean isDeleted;
}
