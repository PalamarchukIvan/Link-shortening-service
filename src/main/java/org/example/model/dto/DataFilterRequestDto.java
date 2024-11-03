package org.example.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataFilterRequestDto {
    private String hash;
    private Long userId;
    private Integer amount;
    private Date startDate;
    private Date endDate;
}
