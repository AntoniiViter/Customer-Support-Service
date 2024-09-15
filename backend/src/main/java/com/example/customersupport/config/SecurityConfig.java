package com.example.customersupport.config;

import com.example.customersupport.service.JpaCorporationDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;



@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JpaCorporationDetailsService myCorporationDetailsService;

    public SecurityConfig(JpaCorporationDetailsService myCorporationDetailsService) {
        this.myCorporationDetailsService = myCorporationDetailsService;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable) //enable in production (disabled for CRUD requests via postmen)
                .formLogin(Customizer.withDefaults())
                .authorizeHttpRequests(
                        authorize -> authorize
                                // Endpoints accessible to authenticated users and admin
                                .requestMatchers(
                                        "/{corpName}/account/**", "/{corpName}/account/**", "/{corpName}/faqs/**", "/{corpName}/clients/**", "/{corpName}/conversations/**")
                                .access(new WebExpressionAuthorizationManager(
                                        "principal.corpName == #corpName or hasRole('ADMIN')"))

                                // Publicly accessible endpoints
                                .requestMatchers("/{corpName}/support/**", "/login", "/registration").permitAll()

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