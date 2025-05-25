package org.example.util.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.ServletException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {

        Optional<String> jwtOpt = Optional.ofNullable(req.getCookies())
                .flatMap(cookies ->
                        Arrays.stream(cookies)
                                .filter(c -> "AUTH_TOKEN".equals(c.getName()))
                                .map(Cookie::getValue)
                                .findFirst()
                );

        if (jwtOpt.isPresent()) {
            try {
                Jws<Claims> claims = jwtService.parseToken(jwtOpt.get());
                String username = claims.getBody().getSubject();

                var userDetails = userDetailsService.loadUserByUsername(username);
                var auth = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (Exception ex) {
                SecurityContextHolder.clearContext();
            }
        }

        chain.doFilter(req, res);
    }
}
