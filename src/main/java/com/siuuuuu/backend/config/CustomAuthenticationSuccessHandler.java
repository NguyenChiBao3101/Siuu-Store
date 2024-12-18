package com.siuuuuu.backend.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    // This method is triggered when a user successfully authenticates
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // Check if the authenticated user has the "ADMIN" authority
        boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"));

        // Check if the authenticated user has the "EMPLOYEE" authority
        boolean isEmployee = authentication.getAuthorities().contains(new SimpleGrantedAuthority("EMPLOYEE"));

        // If the user is an admin or employee, redirect to the admin page
        if (isAdmin || isEmployee) {
            if (isAdmin) {
                response.sendRedirect("/admin/dashboard");
            } else {
                response.sendRedirect("/admin/order");
            }
        }
        // If the user is neither admin nor employee, redirect to the homepage
        else {
            response.sendRedirect("/");
        }
    }
}
