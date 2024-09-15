package com.example.customersupport.controller;

import com.example.customersupport.dto.CorporationRegistrationDto;
import com.example.customersupport.model.relational.Corporation;
import com.example.customersupport.service.CorporationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {
    private final CorporationService corporationService;

    public RegistrationController(CorporationService corporationService) {
        this.corporationService = corporationService;
    }

    @PostMapping("/registration")
    public ResponseEntity<Corporation> registerNewCorporation(@RequestBody CorporationRegistrationDto registrationDto) {
        try {
            Corporation createdCorporation = corporationService.registerNewCorporation(registrationDto);
            return new ResponseEntity<>(createdCorporation, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
