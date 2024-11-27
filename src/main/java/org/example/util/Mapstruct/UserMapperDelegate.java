package org.example.util.Mapstruct;

import org.example.model.User;
import org.example.model.dto.UserFullDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class UserMapperDelegate implements UserMapper {
    @Autowired
    @Qualifier("delegate")
    private UserMapper delegate;
    @Autowired
    private ShortLinkMapper mapper;

    @Override
    public UserFullDto toFullDto(User user) {
        UserFullDto toReturn = delegate.toFullDto(user);
        toReturn.setLinks(
                user.getLinks().stream().map((it) -> mapper.toDto(it)).toList()
        );
        return null;
    }
}
