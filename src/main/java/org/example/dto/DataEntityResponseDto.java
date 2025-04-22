package org.example.dto;

import lombok.*;

import java.time.Instant;
@Builder
@Data
@Setter
@Getter
@AllArgsConstructor
public class DataEntityResponseDto {
    private Instant visitTime;
    private String hash;
    private UserDataResponseDto user;
    private boolean isFound;
}
