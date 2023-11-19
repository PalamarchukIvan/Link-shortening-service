package org.example.util.Mapstruct;

import org.example.model.DataEntity;
import org.example.model.User;
import org.example.model.dto.DataEntityResponseDto;
import org.example.model.dto.UserDataResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper(componentModel = "spring")
public interface DataMapper {
    DataMapper INSTANCE = Mappers.getMapper(DataMapper.class);
//    @Mapping(target = "user", expression = "java(UserMapper.INSTANCE.toDto(dataEntity.getUser()))")
    @Mapping(target = "exists", source = "found")
    DataEntityResponseDto toDto(DataEntity dataEntity);
    List<DataEntityResponseDto> toDto(List<DataEntity> dataEntity);
}
