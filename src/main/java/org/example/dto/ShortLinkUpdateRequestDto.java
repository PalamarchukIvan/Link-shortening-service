package org.example.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShortLinkUpdateRequestDto {
    private String oldHash;
    private String newHash;
}
