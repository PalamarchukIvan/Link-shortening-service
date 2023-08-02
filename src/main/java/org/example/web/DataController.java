package org.example.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.service.DataService.DataService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
public class DataController {
    private final DataService dataService;

    @GetMapping("/stat/statistic")
    public String getGlobalStats(Model model) {
        model.addAttribute("list", dataService.getAll());
        return  "stats_page";
    }

    @GetMapping("/stat/statistic/{hash}")
    public String getLocalStats(@PathVariable String hash, Model model) {
        model.addAttribute("list", dataService.getAllWithHash(hash));
        return  "stats_page";
    }
}
