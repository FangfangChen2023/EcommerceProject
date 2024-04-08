package com.example.ecommerceproject.config;

import com.example.ecommerceproject.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {        // Configuration for Spring security

    private final JwtAuthenticationFilter authenticationFilter;
    private final AuthenticationProvider authenticationProvider;

    // Customize my own security filter chain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/api/auth/**")    // Allow to visit these endpoints for authentication(login in, register page)
                        .permitAll()
                        .requestMatchers("/api/admin").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/api/customer").hasAnyAuthority(Role.CUSTOMER.name())
                        .anyRequest() // Authenticate the rest endpoints
                        .authenticated()
                )
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
                // Add the jwt filter before the UsernamePasswordAuthenticationFilter
                return http.build();
        /***
         * SessionCreationPolicy.STATELESS -   No session will be created or used
         * SessionCreationPolicy.ALWAYS - A session will always be created if it does not exist.
         * SessionCreationPolicy.NEVER - A session will never be created. But if a session exists, it will be used.
         * SessionCreationPolicy.IF_REQUIRED - A session will be created if required. (Default Configuration)
         */

    }
}
