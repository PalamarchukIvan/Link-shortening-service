package org.example.web.restControllers;

import lombok.AllArgsConstructor;
import org.example.dto.DataEntityResponseDto;
import org.example.dto.GetStatisticsDto;
import org.example.dto.UserFullDto;
import org.example.model.User;
import org.example.service.DataService;
import org.example.service.UserService;
import org.example.util.Mapstruct.DataMapper;
import org.example.util.Mapstruct.UserMapper;
import org.example.web.ResultWithStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
@AllArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping(value = "/rest/", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminRestController {
    private final DataService dataService;
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/admin-statistics/")
    public List<DataEntityResponseDto> getAllUserStats() {
        return DataMapper.INSTANCE.toDto(dataService.getFiltered(GetStatisticsDto.EMPTY));

    }

    @GetMapping("/admin-statistics/filtered")
    public List<DataEntityResponseDto> getAllUserFilteredStats(
            @ModelAttribute GetStatisticsDto request,
            @RequestParam(required = false) String login
    ) {
        User user = userService.findActiveByUsername(login).orElseThrow();
        request.setUser(user);
        return DataMapper.INSTANCE.toDto(dataService.getFiltered(request));
    }

    @GetMapping("/reg-log/user")
    public UserFullDto getUser(@RequestParam String login) {
        return userMapper.toFullDto(userService.findActiveByUsername(login)
                .orElseThrow(() -> new RuntimeException("no current user")));
    }
}
