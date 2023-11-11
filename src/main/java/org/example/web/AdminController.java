package org.example.web;

import lombok.RequiredArgsConstructor;
import org.example.service.DataService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final DataService service;
    @GetMapping("/all-stat/statistic")
    public String seeAllStats(Model model) {
        model.addAttribute("list", service.getAll());
        return "full_stats_page";
    }
    @GetMapping("/stat/statistic")
    public String getGlobalStats(Model model, @RequestParam(required = false) Integer amount) {
        if(amount != null) {
            model.addAttribute("list", service.getAll(amount));
        } else {
            model.addAttribute("list", service.getAllByUser());
        }
        return  "stats_page";
    }

    @GetMapping("/stat")
    public String getLocalStats(@RequestParam String hash, Model model, @RequestParam(required = false) Integer amount) {
        if(amount != null) {
            model.addAttribute("list", service.getAllWithHash(hash, amount));
        } else {
            model.addAttribute("list", service.getAllWithHash(hash));
        }
        return  "stats_page";
    }
}
