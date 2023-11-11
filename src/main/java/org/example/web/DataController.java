package org.example.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.User;
import org.example.service.DataService;
import org.example.util.CurrentUserUtil;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
public class DataController {
    private final DataService dataService;

    @GetMapping("/stat/statistic")
    public String getGlobalStats(Model model, @RequestParam(required = false) Integer amount) {
        User user = CurrentUserUtil.getCurrentUser();
        if(amount != null) {
            model.addAttribute("list", dataService.getAll(amount, user));
        } else {
            model.addAttribute("list", dataService.getAllByUser());
        }
        return  "stats_page";
    }

    @GetMapping("/stat/{hash}")
    public String getLocalStats(@PathVariable String hash, Model model, @RequestParam(required = false) Integer amount) {
        User user = CurrentUserUtil.getCurrentUser();
        if(amount != null) {
            model.addAttribute("list", dataService.getAllWithHash(hash, amount, user));
        } else {
            model.addAttribute("list", dataService.getAllWithHash(hash, user));
        }
        return  "stats_page";
    }
}
