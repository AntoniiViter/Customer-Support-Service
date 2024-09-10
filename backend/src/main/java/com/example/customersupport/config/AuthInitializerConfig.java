package com.example.customersupport.config;

import com.example.customersupport.model.auth.AuthGrantedAuthority;
import com.example.customersupport.model.relational.Corporation;
import com.example.customersupport.repository.AuthGrantedAuthorityRepository;
import com.example.customersupport.repository.CorporationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

@Configuration
public class AuthInitializerConfig {

        @Autowired
        private CorporationRepository corporationRepository;

        @Autowired
        private AuthGrantedAuthorityRepository authGrantedAuthorityRepository;

        @Autowired
        private PasswordEncoder passwordEncoder;

        // initialize the admin in DB
        @Bean
        public CommandLineRunner initializeJpaData() {
            return (args) -> {
                // Check if the admin user already exists
                Optional<Corporation> existingAdmin = corporationRepository.findByEmail("admin@admin.com");

                if (existingAdmin.isEmpty()) {  // Only create admin and initial authorities if not already present
                    Corporation admin = new Corporation();
                    admin.setCorpName("admin");
                    admin.setEmail("admin@admin.com");
                    admin.setPassword(passwordEncoder.encode("password"));

                    AuthGrantedAuthority adminAuth = new AuthGrantedAuthority();
                    adminAuth.setAuthority("ROLE_ADMIN");
                    adminAuth.setCorporation(admin);

                    // Save the admin and the associated authority
                    corporationRepository.save(admin);
                    authGrantedAuthorityRepository.save(adminAuth);

                    // Set authority for the admin
                    admin.setAuthorities(Collections.singleton(adminAuth));

                    // Create and save initial authority CORP_MANAGER for managers
                    // Further other authorities inside a corporation can be declared
                    AuthGrantedAuthority corpManagerAuth = new AuthGrantedAuthority();
                    corpManagerAuth.setAuthority("ROLE_CORP_MANAGER");
                    authGrantedAuthorityRepository.save(corpManagerAuth);
                } else {
                    System.out.println("Admin user already exists.");
                }
            };
        }
}
