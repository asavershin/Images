package com.github.asavershin.api.infrastructure.in.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static org.springframework.security.config.http.SessionCreationPolicy.NEVER;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {
    /**
     * Routes that will not be subject to spring security.
     */
    private static final String[] WHITE_LIST_URL = {
            "api/v1/auth/register",
            "api/v1/auth/login",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html",
            "/docs"};
    /**
     * A reference to the JwtAuthenticationFilter instance.
     */
    private final JwtAuthenticationFilter jwtAuthFilter;

    /**
     * A reference to the LogoutHandler instance.
     */
    private final LogoutHandler logoutHandler;

    /**
     * Creates a SecurityFilterChain instance,
     * which is used to secure the application.
     * It configures the security filter chain to disable CSRF protection,
     * allow access to the specified URLs without authentication,
     * and add the JwtAuthenticationFilter
     * before the UsernamePasswordAuthenticationFilter.
     * It also configures the logout functionality
     * to use the specified logout handler and clear
     * the security context after logout.
     *
     * @param http the HttpSecurity instance to configure
     * @return the SecurityFilterChain instance
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(
            final HttpSecurity http
    ) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers(WHITE_LIST_URL)
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                )
                .sessionManagement(
                        session -> session.sessionCreationPolicy(NEVER)
                )
                .addFilterBefore(
                        jwtAuthFilter,
                        UsernamePasswordAuthenticationFilter.class
                )
                .logout(logout ->
                        logout.logoutUrl("/auth/logout")
                                .addLogoutHandler(logoutHandler)
                                .logoutSuccessHandler(
                                        (request, response, authentication)
                                                -> SecurityContextHolder
                                                .clearContext()
                                )
                );
        return http.build();
    }
}
