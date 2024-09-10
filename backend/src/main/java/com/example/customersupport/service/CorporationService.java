package com.example.customersupport.service;

import com.example.customersupport.model.relational.Corporation;
import com.example.customersupport.repository.CorporationRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CorporationService {

    private final CorporationRepository corporationRepository;

    public CorporationService(CorporationRepository corporationRepository) {
        this.corporationRepository = corporationRepository;
    }

    // Check if the corporation exists by its name
    public Optional<Corporation> findByName(String corpName) {
        return corporationRepository.findByCorpName(corpName);
    }

    //TODO: Remaining logic for following endpoints: /faqs, /clients, /conversations
}
