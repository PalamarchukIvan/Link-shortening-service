package org.example.model.dto;

import lombok.*;

import java.time.Instant;
@Builder
@Data
@Setter
@Getter
@AllArgsConstructor
public class DataEntityResponseDto {
    private Instant time;
    private String hash;
    private UserDataResponseDto user;
    private long expectedDuration;
    private boolean exists;
    private long lag;
}

