package org.example.web.restControllers;

import lombok.AllArgsConstructor;
import org.example.model.User;
import org.example.service.UserService;
import org.example.util.CurrentUserUtil;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("http://localhost:3000")
@AllArgsConstructor
@RequestMapping(value = "/rest/reg-log", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserRestController {
    private final UserService service;
    @PostMapping("/login")
    public boolean doLogin(@RequestBody User user) {
        return service.findByUsername((user.getUsername())).isEmpty();
    }
    @GetMapping("/current")
    public User getCurrentUser() {
        return service.findByUsername(CurrentUserUtil.getCurrentUser().getUsername()).orElseThrow(() -> new RuntimeException("no current user"));
    }
    @PatchMapping("/update")
    public User editProfile(@RequestBody User user) {
        return service.updateUser(user);
    }
}
