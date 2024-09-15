package com.example.customersupport.controller;

import com.example.customersupport.model.relational.Corporation;
import com.example.customersupport.service.CorporationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/{corpName}/account")
public class CorporationController {


    private final CorporationService corporationService;

    public CorporationController(CorporationService corporationService) {
        this.corporationService = corporationService;
    }

    @GetMapping
    public ResponseEntity<Corporation> getCorporationByCorpName(@PathVariable String corpName) {
        return corporationService.getCorporationByCorpName(corpName)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new IllegalArgumentException("Corporation not found with corpName: " + corpName));
    }

    @PutMapping
    public ResponseEntity<Corporation> updateCorporation(@PathVariable String corpName, @RequestBody Corporation corporation) {
        Corporation updatedCorporation = corporationService.updateCorporation(corpName, corporation);
        return ResponseEntity.ok(updatedCorporation);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteCorporation(@PathVariable String corpName) {
        corporationService.deleteCorporation(corpName);
        return ResponseEntity.noContent().build();
    }
}