package org.example.web.restControllers;

import lombok.AllArgsConstructor;
import org.example.dto.GetStatisticsDto;
import org.example.facade.DataControllerFacade;
import org.example.dto.DataEntityResponseDto;
import org.example.util.web.ResponseStatusFromResult;
import org.example.web.ResultWithStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
@AllArgsConstructor
@RequestMapping(value = "/rest/statistics", produces = MediaType.APPLICATION_JSON_VALUE)
public class DataRestController {
    private final DataControllerFacade dataControllerFacade;

    @GetMapping("/")
    @ResponseStatusFromResult
    public ResultWithStatus<List<DataEntityResponseDto>> getGlobalStats(@ModelAttribute GetStatisticsDto request) {
        return dataControllerFacade.getStats(request);
    }

}

