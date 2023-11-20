package org.example.web.restControllers;

import lombok.AllArgsConstructor;
import org.example.model.User;
import org.example.service.DataService;
import org.example.util.CurrentUserUtil;
import org.example.util.Mapstruct.DataMapper;
import org.example.util.Mapstruct.UserMapper;
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
    public List<?> getGlobalStats(@RequestParam(required = false) Integer amount,
                                  @RequestParam(required = false) Date startDate,
                                  @RequestParam(required = false) Date endDate) {
        User user = CurrentUserUtil.getCurrentUser();
        if(amount != null) {
            return DataMapper.INSTANCE.toDto(dataService.getAll(amount, user, startDate, endDate));
        }
        return DataMapper.INSTANCE.toDto(dataService.getAllByUser(user, startDate, endDate));

    }

    @GetMapping("/hash")
    public List<?> getLocalStats(@RequestParam String hash,
                                 @RequestParam(required = false) Integer amount,
                                 @RequestParam(required = false) Date startDate,
                                 @RequestParam(required = false) Date endDate) {
        User user = CurrentUserUtil.getCurrentUser();
        if(amount != null) {
            return DataMapper.INSTANCE.toDto(dataService.getAllWithHash(hash, amount, user, startDate, endDate));
        }
        return DataMapper.INSTANCE.toDto(dataService.getAllWithHash(hash, user, startDate, endDate));

    }
}

