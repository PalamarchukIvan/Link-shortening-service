package org.example.util.Mapstruct;

import org.example.model.User;
import org.example.model.dto.UserDataResponseDto;
import org.example.model.dto.UserFullDto;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
@Mapper(componentModel = "spring", uses = {ShortLinkMapper.class})
@DecoratedWith(UserMapperDelegate.class)
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    UserDataResponseDto toDto(User user);

    @Mapping(target = "links", ignore = true)
    UserFullDto toFullDto(User user);
}
