package com.siuuuuu.backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import com.siuuuuu.backend.dto.response.ErrorResponse;
import java.io.IOException;

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // Set the response content type to JSON
        response.setContentType("application/json");

        // Set the HTTP status to 401 Unauthorized
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        // Create an ErrorResponse object with the status code and exception message
        ErrorResponse<?> errorResponse = new ErrorResponse<>(HttpStatus.UNAUTHORIZED.value(), authException.getMessage(), null);

        // Write the error response to the output stream
        response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));

        // Ensure the response is flushed to the client
        response.flushBuffer();
    }
}
