package org.example.facade;

import lombok.RequiredArgsConstructor;
import org.example.dto.DataEntityResponseDto;
import org.example.dto.GetStatisticsDto;
import org.example.dto.UserFullDto;
import org.example.model.User;
import org.example.service.DataService;
import org.example.service.UserService;
import org.example.util.Mapstruct.DataMapper;
import org.example.util.Mapstruct.UserMapper;
import org.example.web.ResultWithStatus;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminControllerFacade {
    private final DataService dataService;
    private final UserService userService;
    private final UserMapper userMapper;

    public ResultWithStatus<List<DataEntityResponseDto>> getAllUserFilteredStats(GetStatisticsDto request, String login) {
        Optional<User> user = userService.findActiveByUsername(login);
        if (login != null && !login.isEmpty() && user.isEmpty()) {
            return ResultWithStatus.ok(List.of());
        }
        user.ifPresent(request::setUser);
        return ResultWithStatus.ok(DataMapper.INSTANCE.toDto(dataService.getFiltered(request)));
    }

    public ResultWithStatus<UserFullDto> getUser(String login) {
        Optional<User> actualInDb = userService.findActiveByUsername(login);
        if (!actualInDb.isPresent()) {
            return ResultWithStatus.error(HttpStatus.BAD_REQUEST, "User not found");
        }
        return ResultWithStatus.ok(userMapper.toFullDto(actualInDb.get()));
    }
}
