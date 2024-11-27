package org.example.web.restControllers;

import lombok.AllArgsConstructor;
import org.example.model.DataEntity;
import org.example.model.User;
import org.example.model.dto.DataEntityResponseDto;
import org.example.service.DataService;
import org.example.util.CurrentUserUtil;
import org.example.util.Mapstruct.DataMapper;
import org.example.util.Mapstruct.UserMapper;
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
    public List<DataEntityResponseDto> getGlobalStats(@RequestParam(required = false) Integer amount,
                                  @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date startDate,
                                  @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date endDate) {
        User user = CurrentUserUtil.getCurrentUser();
        if(amount != null) {
            return DataMapper.INSTANCE.toDto(dataService.getAll(amount, user, startDate, endDate));
        }
        return DataMapper.INSTANCE.toDto(dataService.getAllByUser(user, startDate, endDate));

    }

    @GetMapping("/hash")
    public List<DataEntityResponseDto> getLocalStats(@RequestParam String hash,
                                                     @RequestParam(required = false) Integer amount,
                                                     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)Date startDate,
                                                     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date endDate) {
        User user = CurrentUserUtil.getCurrentUser();
        if(amount != null) {
            return DataMapper.INSTANCE.toDto(dataService.getAllWithHash(hash, amount, user, startDate, endDate));
        }
        return DataMapper.INSTANCE.toDto(dataService.getAllWithHash(hash, user, startDate, endDate));

    }
}

