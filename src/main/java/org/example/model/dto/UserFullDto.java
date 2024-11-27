package org.example.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.DataEntity;
import org.example.model.Role;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserFullDto {
    private Long id;
    private String username;
    private String name;
    private Boolean isActive;
    private List<Role> role;
    private List<ShortLinkDto> links;
    private List<DataEntity> data;
}
