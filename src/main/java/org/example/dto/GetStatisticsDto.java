package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.User;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class GetStatisticsDto {
    private Integer amount;
    private User user;
    private Date startDate;
    private Date endDate;
    private String hash;

    public static GetStatisticsDto EMPTY = new GetStatisticsDto();

}
