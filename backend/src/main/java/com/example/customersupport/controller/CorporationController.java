package com.example.customersupport.controller;


import com.example.customersupport.service.CorporationService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class CorporationController {

    private final CorporationService corporationService;
    
    
    public CorporationController(CorporationService corporationService) {
        this.corporationService = corporationService;
    }
    @GetMapping("/{corp_name}/chat/**")
    public String chat(@PathVariable String corp_name) {
        //TODO
        return "TODO: Chat page for corporation: " + corp_name;
    }

    // Authenticated Endpoints

    @GetMapping("/{corp_name}/clients")
    public String clients(@PathVariable String corp_name, Authentication authentication) {
        //TODO
        if (corporationService.findByName(corp_name).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Corporation not found");
        }
        return "TODO: Clients page for user with corp_name: " + corp_name + ". Authenticated as: " + authentication.getName();
    }

    @GetMapping("/{corp_name}/faqs")
    public String faqs(@PathVariable String corp_name, Authentication authentication) {
        //TODO
        if (corporationService.findByName(corp_name).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Corporation not found");
        }
        return "TODO: FAQs page for user with corp_name: " + corp_name + ". Authenticated as: " + authentication.getName();
    }

    @GetMapping("/{corp_name}/account")
    public String account(@PathVariable String corp_name, Authentication authentication) {
        //TODO
        if (corporationService.findByName(corp_name).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Corporation not found");
        }
        return "TODO: Account page for user with corp_name: " + corp_name + ". Authenticated as: " + authentication.getName();
    }

    @GetMapping("/{corp_name}/conversations")
    public String conversations(@PathVariable String corp_name) {
        // Placeholder response for now
        if (corporationService.findByName(corp_name).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Corporation not found");
        }
        return "TODO: List of conversations for corporation with corp_name: " + corp_name;
    }

    // Admin-Only Endpoint

    @GetMapping("/corporations")
    public String corporations() {
        //TODO
        return "TODO: Corporations page for Admin only";
    }

    //TODO: According post and put endpoints
}
