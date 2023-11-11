package org.example.util;

import org.example.model.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class CurrentUserUtil {
    public static User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
