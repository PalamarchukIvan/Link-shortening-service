package org.example.web.restControllers;

import lombok.AllArgsConstructor;
import org.example.model.User;
import org.example.service.DataService;
import org.example.util.CurrentUserUtil;
import org.example.util.Mapstruct.DataMapper;
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
@RequestMapping(value = "/rest/admin-statistics", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminRestController {
    private final DataService dataService;
    @GetMapping("/")
    public List<?> getAllUserStats() {
        return DataMapper.INSTANCE.toDto(dataService.getAll());

    }
    @GetMapping("/filters")
    public List<?> getAllUserFilteredStats(@RequestParam String hash,
                                   @RequestParam(required = false) Integer amount,
                                   @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date startDate,
                                   @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date endDate) {
        User user = CurrentUserUtil.getCurrentUser();
        if(amount != null) {
            return DataMapper.INSTANCE.toDto(dataService.getAllWithHash(hash, amount, user, startDate, endDate));
        }
        return DataMapper.INSTANCE.toDto(dataService.getAllWithHash(hash, user, startDate, endDate));

    }
}
