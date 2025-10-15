package com.siuuuuu.backend.security;

import com.siuuuuu.backend.service.JwtService;
import com.siuuuuu.backend.service.TokenStoreService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final TokenStoreService tokenStoreService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            SecurityContextHolder.clearContext();
            chain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);
        // 1) token phải còn hạn
        if (!jwtService.isValidAndNotExpired(token)) {
            SecurityContextHolder.clearContext();
            chain.doFilter(request, response);
            return;
        }

        // 2) không bị revoke
        String jti = jwtService.getJti(token);
        if (tokenStoreService.isRevoked(jti)) {
            SecurityContextHolder.clearContext();
            chain.doFilter(request, response);
            return;
        }

        String username = jwtService.getUsername(token);
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails user = userDetailsService.loadUserByUsername(username);
            var auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        chain.doFilter(request, response);
    }
}

