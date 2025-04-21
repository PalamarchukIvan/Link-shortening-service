package org.example.facade;

import lombok.AllArgsConstructor;
import org.example.dto.ShortLinkDto;
import org.example.dto.ShortLinkRequestDto;
import org.example.dto.ShortLinkUpdateRequestDto;
import org.example.model.ShortLink;
import org.example.model.User;
import org.example.service.ShortLinkService;
import org.example.service.UserService;
import org.example.util.CurrentUserUtil;
import org.example.util.Mapstruct.ShortLinkMapper;
import org.example.web.ResultWithStatus;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ShortLinkControllerFacade {
    
    private UserService userService;
    private ShortLinkMapper mapper;
    
    private ShortLinkService shortLinkService;
    
    public ResultWithStatus<List<ShortLinkDto>> getShortLinksByUser() {
        Optional<User> user = userService.findActiveByUsername(CurrentUserUtil.getCurrentUser().getUsername());
        if (!user.isPresent()) {
            return ResultWithStatus.error(HttpStatus.BAD_REQUEST, "Session is not logged in");
        }
        user.get().getLinks().removeIf(ShortLink::isDeleted);
        return ResultWithStatus.ok(mapper.toDto(user.get().getLinks()));
    }

    public ResultWithStatus<ShortLinkDto> createShortLink(ShortLinkRequestDto shortLinkRequestDto) {
        return ResultWithStatus.ok(mapper.toDto(shortLinkService.create(mapper.fromRequest(shortLinkRequestDto))));
    }


    public ResultWithStatus<ShortLinkDto> updateShortLink(ShortLinkUpdateRequestDto shortLinkRequestDto) {
        return ResultWithStatus.ok(mapper.toDto(shortLinkService.update(shortLinkRequestDto)));
    }

    public ResultWithStatus deleteByHash(String hash) {
        shortLinkService.deleteByHash(hash);
        return ResultWithStatus.ok();
    }
    
}
