package org.example.util.Mapstruct;

import org.example.model.ShortLink;
import org.example.model.dto.ShortLinkDto;
import org.example.model.dto.ShortLinkRequestDto;
import org.example.model.dto.ShortLinkResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ShortLinkMapper {
    ShortLink fromRequest(ShortLinkRequestDto requestDto);
    @Mapping(target = "link", expression = "java( \"http://localhost:8080/s/\".concat(shortLink.getHash()) )")
    ShortLinkResponseDto toResponse(ShortLink shortLink);
    ShortLinkDto toDto(ShortLink shortLink);
    List<ShortLinkDto> toDto(List<ShortLink> shortLink);
}
