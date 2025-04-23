package org.example.web.restControllers;

import lombok.AllArgsConstructor;
import org.example.dto.DataEntityResponseDto;
import org.example.dto.GetStatisticsDto;
import org.example.dto.UserFullDto;
import org.example.facade.AdminControllerFacade;
import org.example.facade.DataControllerFacade;
import org.example.util.web.ResponseStatusFromResult;
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
    private final AdminControllerFacade adminControllerFacade;

    @GetMapping("/admin-statistics/")
    @ResponseStatusFromResult
    public ResultWithStatus< List<DataEntityResponseDto>> getAllUserFilteredStats(
            @ModelAttribute GetStatisticsDto request,
            @RequestParam(required = false) String login
    ) {
        return adminControllerFacade.getAllUserFilteredStats(request, login);
    }

    @GetMapping("/reg-log/user")
    @ResponseStatusFromResult
    public ResultWithStatus<UserFullDto> getUser(@RequestParam String login) {
        return adminControllerFacade.getUser(login);
    }
}
