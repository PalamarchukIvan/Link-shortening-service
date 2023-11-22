package org.example.model.dto;

import lombok.*;
import org.example.model.Role;

import java.util.List;

@Builder
@Data
@Getter
@Setter
@AllArgsConstructor
public class UserDataResponseDto {
    private Long id;
    private String name;
    private String username;
    private List<Role> role;
    private String password;
    private Boolean isActive;
}
