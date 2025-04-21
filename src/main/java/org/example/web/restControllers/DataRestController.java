package org.example.web.restControllers;

import lombok.AllArgsConstructor;
import org.example.dto.GetStatisticsDto;
import org.example.model.User;
import org.example.dto.DataEntityResponseDto;
import org.example.service.DataService;
import org.example.util.CurrentUserUtil;
import org.example.util.Mapstruct.DataMapper;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
@AllArgsConstructor
@RequestMapping(value = "/rest/statistics", produces = MediaType.APPLICATION_JSON_VALUE)
public class DataRestController {
    private final DataService dataService;

    @GetMapping("/")
    public List<DataEntityResponseDto> getGlobalStats(@ModelAttribute GetStatisticsDto request) {
        User user = CurrentUserUtil.getCurrentUser();
        request.setUser(user);
        return DataMapper.INSTANCE.toDto(dataService.getFiltered(request));
    }

}

