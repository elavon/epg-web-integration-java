package com.elavon.epg.web.integration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig
{
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
    {
        http
                .authorizeHttpRequests((authz) -> authz
                        .anyRequest().permitAll()
                )
                .headers(headers -> headers
                        .contentSecurityPolicy( csp -> csp
                                .policyDirectives("default-src 'self'; " +
                                        "script-src 'self' https://cdn.jsdelivr.net/ https://uat.hpp.converge.eu.elavonaws.com/ 'unsafe-inline'; " +
                                        "style-src 'self' https://cdn.jsdelivr.net/ 'unsafe-inline'; " +
                                        "img-src 'self' data:;" +
                                        "frame-src 'self' https://uat.hpp.converge.eu.elavonaws.com/;")
                        )
                );

        return http.build();
    }
}