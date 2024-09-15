package com.example.customersupport.controller;

import com.example.customersupport.model.relational.FAQ;
import com.example.customersupport.service.FAQService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/{corpName}/faqs")
public class FAQController {

    private final FAQService faqService;

    public FAQController(FAQService faqService) {
        this.faqService = faqService;
    }

    @PostMapping
    public ResponseEntity<FAQ> createFAQ(@PathVariable String corpName, @RequestBody FAQ faq) {
        FAQ createdFaq = faqService.createFAQ(corpName, faq);
        return ResponseEntity.ok(createdFaq);
    }

    @PostMapping("/multiple")
    public ResponseEntity<List<FAQ>> createFAQs(@PathVariable String corpName, @RequestBody List<FAQ> faqs) {
        List<FAQ> savedFAQs = faqService.createFAQs(corpName, faqs);
        return new ResponseEntity<>(savedFAQs, HttpStatus.CREATED);
    }

    @PutMapping("/{faqId}")
    public ResponseEntity<FAQ> updateFAQ(@PathVariable Long faqId, @RequestBody FAQ faq) {
        FAQ updatedFaq = faqService.updateFAQ(faqId, faq);
        return ResponseEntity.ok(updatedFaq);
    }

    @DeleteMapping("/{faqId}")
    public ResponseEntity<Void> deleteFAQ(@PathVariable Long faqId) {
        faqService.deleteFAQ(faqId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<FAQ>> getFAQs(@PathVariable String corpName) {
        List<FAQ> faqs = faqService.getFAQsByCorporation(corpName);
        return ResponseEntity.ok(faqs);
    }
}