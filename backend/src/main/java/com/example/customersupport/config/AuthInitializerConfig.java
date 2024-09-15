package com.example.customersupport.config;

import com.example.customersupport.dto.CorporationRegistrationDto;
import com.example.customersupport.model.auth.AuthGrantedAuthority;
import com.example.customersupport.model.relational.Client;
import com.example.customersupport.model.relational.Corporation;
import com.example.customersupport.model.relational.FAQ;
import com.example.customersupport.repository.AuthGrantedAuthorityRepository;
import com.example.customersupport.service.CorporationService;
import com.example.customersupport.service.FAQService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
public class AuthInitializerConfig {

        private final CorporationService corporationService;

        private final AuthGrantedAuthorityRepository authGrantedAuthorityRepository;

        private final FAQService faqService;

    public AuthInitializerConfig(CorporationService corporationService, AuthGrantedAuthorityRepository authGrantedAuthorityRepository, FAQService faqService) {
        this.corporationService = corporationService;
        this.authGrantedAuthorityRepository = authGrantedAuthorityRepository;
        this.faqService = faqService;
    }

    // initialize the admin in DB
        @Bean
        public CommandLineRunner initializeJpaData() {
            return (args) -> {
                // Check if the admin user already exists
                Optional<Corporation> existingAdmin = corporationService.getCorporationByCorpName("admin");

                if (existingAdmin.isEmpty()) {  // Only create admin and initial authorities if not already present
                    CorporationRegistrationDto adminDTO = new CorporationRegistrationDto();
                    adminDTO.setCorpName("admin");
                    adminDTO.setEmail("admin@example.com");
                    adminDTO.setPassword("password");

                    // Save the admin and the associated authority
                    Corporation admin = corporationService.registerNewCorporation(adminDTO);

                    //for convenience
                    List<FAQ> faqList = new ArrayList<>();

                    FAQ faq1 = new FAQ();
                    faq1.setQuestion("How can I reset my password?");
                    faq1.setAnswer("To reset your password, click on the 'Forgot Password' link on the login page and follow the instructions.");
                    faq1.setCorporation(admin);
                    faqList.add(faq1);

                    FAQ faq2 = new FAQ();
                    faq2.setQuestion("What payment methods do you accept?");
                    faq2.setAnswer("We accept Visa, MasterCard, American Express, PayPal, and bank transfers.");
                    faq2.setCorporation(admin);
                    faqList.add(faq2);

                    FAQ faq3 = new FAQ();
                    faq3.setQuestion("How do I track my order?");
                    faq3.setAnswer("You can track your order by logging into your account and navigating to the 'Orders' section.");
                    faq3.setCorporation(admin);
                    faqList.add(faq3);

                    FAQ faq4 = new FAQ();
                    faq4.setQuestion("Can I change or cancel my order?");
                    faq4.setAnswer("You can change or cancel your order within 24 hours of placing it by contacting our support team.");
                    faq4.setCorporation(admin);
                    faqList.add(faq4);

                    FAQ faq5 = new FAQ();
                    faq5.setQuestion("What is your return policy?");
                    faq5.setAnswer("We offer a 30-day return policy. Items must be unused and in their original packaging.");
                    faq5.setCorporation(admin);
                    faqList.add(faq5);

                    FAQ faq6 = new FAQ();
                    faq6.setQuestion("How do I contact customer support?");
                    faq6.setAnswer("You can contact our customer support via email at support@example.com or call us at 1-800-123-4567.");
                    faq6.setCorporation(admin);
                    faqList.add(faq6);

                    FAQ faq7 = new FAQ();
                    faq7.setQuestion("Do you offer international shipping?");
                    faq7.setAnswer("Yes, we offer international shipping to select countries. Shipping fees and delivery times vary by location.");
                    faq7.setCorporation(admin);
                    faqList.add(faq7);
                    faqService.createFAQs(admin.getCorpName(), faqList);

                    Client anonymousClient = new Client();
                    anonymousClient.setCorporation(admin);
                    anonymousClient.setEmail("anonymous@example.com");
                    admin.setClients(List.of(anonymousClient));
                    admin.setFaqs(faqList);
                    corporationService.updateCorporation(admin.getCorpName(), admin);

                    //Authorities
                    AuthGrantedAuthority adminAuth = new AuthGrantedAuthority();
                    adminAuth.setAuthority("ROLE_ADMIN");
                    adminAuth.setCorporation(admin);
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
