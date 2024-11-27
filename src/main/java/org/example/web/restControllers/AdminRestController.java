package org.example.web.restControllers;

import lombok.AllArgsConstructor;
import org.example.model.User;
import org.example.model.dto.DataEntityResponseDto;
import org.example.model.dto.UserFullDto;
import org.example.service.DataService;
import org.example.service.UserService;
import org.example.util.CurrentUserUtil;
import org.example.util.Mapstruct.DataMapper;
import org.example.util.Mapstruct.UserMapper;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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
        return DataMapper.INSTANCE.toDto(dataService.getAll());

    }
    @GetMapping("/admin-statistics/filtered")
    public List<DataEntityResponseDto> getAllUserFilteredStats(
                                   @RequestParam(required = false) String hash,
                                   @RequestParam(required = false) Integer amount,
                                   @RequestParam(required = false) String login,
                                   @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date startDate,
                                   @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date endDate) {
        if((hash == null || hash.equals("")) && amount == null && (login == null || login.equals("")) && startDate == null && endDate == null) {
            return getAllUserStats();
        }
        User user = userService.findByUsername(login).orElseThrow();
        return DataMapper.INSTANCE.toDto(dataService.getAllWithFilter(amount, user, startDate, endDate, hash));
    }
    @GetMapping("/reg-log/user")
    public UserFullDto getCurrentUser(@RequestParam String login) {
        return userMapper.toFullDto(userService.findByUsername(login)
                .orElseThrow(() -> new RuntimeException("no current user")));
    }
}
