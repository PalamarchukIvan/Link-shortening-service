package org.example.util.Mapstruct;

import javax.annotation.processing.Generated;
import org.example.model.ShortLink;
import org.example.model.dto.ShortLinkRequestDto;
import org.example.model.dto.ShortLinkResponseDto;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-11-21T15:19:26+0200",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.7 (Oracle Corporation)"
)
@Component
public class ShortLinkMapperImpl implements ShortLinkMapper {

    @Override
    public ShortLink fromRequest(ShortLinkRequestDto requestDto) {
        if ( requestDto == null ) {
            return null;
        }

        ShortLink.ShortLinkBuilder shortLink = ShortLink.builder();

        shortLink.link( requestDto.getLink() );

        return shortLink.build();
    }

    @Override
    public ShortLinkResponseDto toResponse(ShortLink shortLink) {
        if ( shortLink == null ) {
            return null;
        }

        ShortLinkResponseDto shortLinkResponseDto = new ShortLinkResponseDto();

        shortLinkResponseDto.link = "http://localhost:8080/s/".concat(shortLink.getHash());

        return shortLinkResponseDto;
    }
}
