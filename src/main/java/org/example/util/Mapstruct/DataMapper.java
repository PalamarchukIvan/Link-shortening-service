package org.example.util.Mapstruct;

import org.example.model.DataEntity;
import org.example.model.Role;
import org.example.model.User;
import org.example.model.dto.DataEntityResponseDto;
import org.example.model.dto.UserDataResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;


@Mapper(componentModel = "spring")
public interface DataMapper {
    DataMapper INSTANCE = Mappers.getMapper(DataMapper.class);

    static DataEntityResponseDto toDto(DataEntity dataEntity) {
        if ( dataEntity == null ) {
            return null;
        }

        DataEntityResponseDto.DataEntityResponseDtoBuilder dataEntityResponseDto = DataEntityResponseDto.builder();

        dataEntityResponseDto.exists( dataEntity.isFound() );
        dataEntityResponseDto.time( dataEntity.getTime() );
        dataEntityResponseDto.hash( dataEntity.getHash() );
        dataEntityResponseDto.user( userToUserDataResponseDto( dataEntity.getUser() ) );
        dataEntityResponseDto.expectedDuration( dataEntity.getExpectedDuration() );
        dataEntityResponseDto.lag( dataEntity.getLag() );

        return dataEntityResponseDto.build();
    }
    private static UserDataResponseDto userToUserDataResponseDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserDataResponseDto.UserDataResponseDtoBuilder userDataResponseDto = UserDataResponseDto.builder();

        userDataResponseDto.id( user.getId() );
        userDataResponseDto.name( user.getName() );
        List<Role> list = user.getRole();
        if ( list != null ) {
            userDataResponseDto.role( new ArrayList<Role>( list ) );
        }
        userDataResponseDto.username( user.getUsername() );
        userDataResponseDto.isActive( user.getIsActive() );

        return userDataResponseDto.build();
    }
    List<DataEntityResponseDto> toDto(List<DataEntity> dataEntity);
}
