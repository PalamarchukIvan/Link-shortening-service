package org.example.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.example.model.Role;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
public class UserDataResponseDto {
    private Long id;
    private String name;
    private String loginName;
    private List<Role> role;
    private String password;
    private Boolean isActive;
}
