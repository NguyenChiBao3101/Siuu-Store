package com.siuuuuu.backend.config;

import com.siuuuuu.backend.entity.Account;
import com.siuuuuu.backend.repository.AccountRepository;
import com.siuuuuu.backend.security.JwtAuthenticationFilter;
import com.siuuuuu.backend.security.RestAccessDeniedHandler;
import com.siuuuuu.backend.security.RestAuthenticationEntryPoint;
import com.siuuuuu.backend.service.RecaptchaService;
import com.siuuuuu.backend.service.CustomOAuth2UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.RequestCacheConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This class defines the security configuration for the application.
 * It sets up authentication, authorization, password encoding, and other security-related configurations.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {
    @Value("${spring.security.rsa.privatekey}")
    private String privateKeyBase64;
    // URLs that do not require authentication (whitelist)
    private static final String[] WHITE_LIST_URL = {
            "/",                  // Home page
            "/auth/sign-in",       // Sign-in page
            "/auth/sign-up",
            "/auth/verify",
            "/oauth2/**",
            "assets/**",
            "/shop/**",
            "/swagger-ui/index.html",
            "/actuator/health", "/actuator/health/**"
    };
    /* ===== Exception handlers as beans (tiêm vào HttpSecurity) ===== */

    @Bean
    public RestAuthenticationEntryPoint restAuthenticationEntryPoint() {
        return new RestAuthenticationEntryPoint();
    }

    @Bean
    public RestAccessDeniedHandler restAccessDeniedHandler() {
        return new RestAccessDeniedHandler();
    }

    /* ===== CORS cho API ===== */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowedOriginPatterns(List.of("*"));
        cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        cfg.setAllowedHeaders(List.of("*"));
        cfg.setExposedHeaders(List.of("Authorization", "Content-Type"));
        cfg.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }

    private final RecaptchaService recaptchaService;

    public SecurityConfiguration(RecaptchaService recaptchaService) {
        this.recaptchaService = recaptchaService;
    }

    // CHAIN 1: API (/api/**)
    @Bean
    @Order(0)
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http,
                                                      JwtAuthenticationFilter jwtAuthenticationFilter,
                                                      RestAuthenticationEntryPoint authEntryPoint,
                                                      RestAccessDeniedHandler accessDeniedHandler) throws Exception {
        http
                .securityMatcher("/api/v1/**")
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.POST,
                                "/api/v1/auth/register",
                                "/api/v1/auth/login",
                                "/api/v1/auth/refresh"

                        ).permitAll()
                        .requestMatchers(HttpMethod.GET,
                                "/api/v1/accounts/**",
                                "/api/v1/products/**"
                        ).permitAll()
                        .requestMatchers(HttpMethod.PUT,
                                "/api/v1/accounts/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(authEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )
                .requestCache(RequestCacheConfigurer::disable)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }

    /**
     * Configures the security filter chain, which defines how HTTP requests are secured.
     *
     * @param http the HttpSecurity object used to configure security
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs during configuration
     */
    // CHAIN 2: MVC (phần còn lại)
    @Bean
    @Order(1)
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomOAuth2UserService customOAuth2UserService) throws Exception {
        //CSRF protection and configure request authorization
        http.addFilterBefore(new RecaptchaAuthenticationFilter(recaptchaService), UsernamePasswordAuthenticationFilter.class)
                .csrf(Customizer.withDefaults())
                .authorizeHttpRequests(req -> req
                        .requestMatchers(WHITE_LIST_URL).permitAll()            // Permit requests to the whitelist URLs
                        .requestMatchers("/admin/**").hasAnyAuthority("ADMIN", "EMPLOYEE")  // Users with 'ADMIN' and Employee role can access admin routes
                        .anyRequest().authenticated()                           // All other requests require authentication
                ).oauth2Login(oauth2 -> oauth2
                        .loginPage("/auth/sign-in")
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(
                                        customOAuth2UserService
                                ) // Use your custom service
                        )
                        .defaultSuccessUrl("/", true)// Redirect after successful Google login


                )
                // Configure form-based login
                .formLogin((form) -> form
                        .loginPage("/auth/sign-in")                             // Custom login page URL
                        .loginProcessingUrl("/auth/sign-in")                    // URL to submit login credentials
                        .failureUrl("/auth/sign-in?error=true")                      // Redirect on login failure
                        .defaultSuccessUrl("/", true)                           // Redirect to home on successful login
                        .successHandler(new CustomAuthenticationSuccessHandler()) // Custom success handler for additional actions after login
                        .permitAll()                                            // Allow everyone to access the login page
                )
                .rememberMe((rememberMe) -> rememberMe
                        .key("3cfa76ef14937c1c0ea519f8fc057a80fcd04a7420f8e8bcd0a7567c272e007b")                                 // Key for remember-me cookie
                        .tokenValiditySeconds(86400)                            // Token validity period (24 hours)
                        .rememberMeParameter("remember-me")                     // Form parameter for remember-me
                        .rememberMeCookieName(".APPAUTH")                   // Cookie name for remember-me
                )
                // Configure logout settings
                .logout((logout) -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/auth/sign-out", "GET")) // Allow GET for logout                                           // URL for logging out
                        .logoutSuccessUrl("/auth/sign-in")                                          // Redirect to home page after logout
                        .deleteCookies("JSESSIONID", ".APPAUTH")                // Delete cookies on logout
                        .invalidateHttpSession(true)                                               // Invalidate session on logout
                        .clearAuthentication(true)                                                  // Clear authentication on logout
                        .permitAll()                                                                // Allow everyone to log out
                )
                // Configure handling of access denied exceptions
                .exceptionHandling((exception) -> exception
                        .accessDeniedPage("/auth/access-denied")                // Redirect to a custom access denied page
                );

        return http.build();
    }

    /**
     * Configures a password encoder using BCrypt algorithm.
     * BCrypt is a secure hashing algorithm for storing passwords.
     *
     * @return the BCryptPasswordEncoder instance
     */
    /** Encoder cho MVC (form login) */
    @Bean
    public PasswordEncoder passwordEncoder() throws Exception {
        return new RsaBcryptPasswordEncoder(privateKeyBase64);
    }
    /** Encoder cho API (plaintext → BCrypt). */
    @Bean(name = "plainPasswordEncoder")
    public PasswordEncoder plainPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    /**
     * Configures a custom UserDetailsService to load user-specific data.
     * This service fetches user details from the database (via AccountRepository) during login.
     *
     * @param accountRepository repository to fetch user account data
     * @return the UserDetailsService instance
     */
    @Bean
    public UserDetailsService userDetailsService(AccountRepository accountRepository) {
        return username -> {
            // Find user by email (username) in the repository
            Account account = accountRepository.findByEmail(username);
            if (account == null) {
                // Throw exception if user not found
                throw new UsernameNotFoundException("User not found");
            }

            if (Boolean.FALSE.equals(account.getIsActive())) {
                throw new DisabledException("Account is inactive");
            }

            // Check account verification status
            if (Boolean.FALSE.equals(account.getIsVerified())) {
                throw new DisabledException("Account is not verified");
            }

            // Return a User object containing email, password, and roles
            return new org.springframework.security.core.userdetails.User(
                    account.getEmail(),
                    account.getPassword(),
                    account.getRoles().stream()   // Map roles to authorities
                            .map(role -> new SimpleGrantedAuthority(role.getName()))
                            .collect(Collectors.toList())
            );
        };
    }
}
