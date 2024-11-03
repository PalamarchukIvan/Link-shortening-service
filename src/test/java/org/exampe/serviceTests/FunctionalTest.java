package org.exampe.serviceTests;

import org.example.model.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

public class FunctionalTest {

    User currentUser = User.builder()
            .username("name")
            .password("pass")
            .isActive(true)
            .build();

    public FunctionalTest() {
        SecurityContextHolder.setContext(new SecurityContextImpl(new UsernamePasswordAuthenticationToken(currentUser, null)));
    }



}
