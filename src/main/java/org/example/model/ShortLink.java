package org.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ShortLinks", indexes = @Index(name = "idx_hashcode", columnList = "hash"))
public class ShortLink {
    @Id
    private String hash;
    private String link;
    private boolean isDeleted;
}
