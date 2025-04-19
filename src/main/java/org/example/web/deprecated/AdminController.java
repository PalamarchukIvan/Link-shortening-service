package org.example.web.deprecated;

import lombok.RequiredArgsConstructor;
import org.example.dto.GetStatisticsDto;
import org.example.service.DataService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final DataService service;

    @GetMapping("/stat")
    public String getLocalStats(Model model, @RequestParam(required = false) @ModelAttribute GetStatisticsDto request) {
        model.addAttribute("list", service.getFiltered(request));
        return  "stats_page";
    }
}
