package org.example.util.Mapstruct;

import org.example.model.ShortLink;
import org.example.model.dto.ShortLinkRequestDto;
import org.example.model.dto.ShortLinkResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ShortLinkMapper {
    ShortLink fromRequest(ShortLinkRequestDto requestDto);
    @Mapping(target = "link", expression = "java( \"http://localhost:8080/shortening_api/\".concat(shortLink.getHash()) )")
    ShortLinkResponseDto toResponse(ShortLink shortLink);
}
