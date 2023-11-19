package org.example.util.Mapstruct;

import org.example.model.User;
import org.example.model.dto.UserDataResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    UserDataResponseDto toDto(User user);
}
