package org.example.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShortLinkRequestDto {
    private String hash;
    private String link;
}
