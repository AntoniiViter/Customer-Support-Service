package com.example.customersupport.config;

import com.example.customersupport.service.JpaCorporationDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JpaCorporationDetailsService myCorporationDetailsService;

    public SecurityConfig(JpaCorporationDetailsService myCorporationDetailsService) {
        this.myCorporationDetailsService = myCorporationDetailsService;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.formLogin(Customizer.withDefaults())
                .authorizeHttpRequests(
                        authorize -> authorize
                                // Publicly accessible endpoints
                                .requestMatchers("/login", "/registration", "/{corp_name}/chat/**").permitAll()

                                // Endpoints accessible to authenticated users and admin
                                .requestMatchers("/{corp_name}/clients", "/{corp_name}/faqs", "/{corp_name}/account", "/{corp_name}/conversations")
                                .access(new WebExpressionAuthorizationManager(
                                        "hasRole('ADMIN') or #corp_name == authentication.principal.corpName"))

                                // Endpoint only accessible to admin
                                .requestMatchers("/corporations").hasRole("ADMIN")

                                // Any other request must be authenticated (e.g. logout)
                                .anyRequest().authenticated()
                )
                .userDetailsService(myCorporationDetailsService)
                .build();
    }


    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}