package com.siuuuuu.backend.config;

import com.siuuuuu.backend.service.RecaptchaService;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class RecaptchaAuthenticationFilter implements Filter {

    private final RecaptchaService recaptchaService;

    public RecaptchaAuthenticationFilter(RecaptchaService recaptchaService) {
        this.recaptchaService = recaptchaService;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Không cần cấu hình gì thêm
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if ("POST".equalsIgnoreCase(httpRequest.getMethod()) && "/auth/sign-in".equals(httpRequest.getServletPath())) {
            String captchaResponse = httpRequest.getParameter("g-recaptcha-response");
            if (!recaptchaService.validateCaptcha(captchaResponse)) {
                httpResponse.sendRedirect("/auth/sign-in?error-captcha=true");
                return;
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Không cần xử lý thêm
    }
}
