package com.example.customersupport.service;

import com.example.customersupport.model.relational.Corporation;
import com.example.customersupport.model.relational.FAQ;
import com.example.customersupport.repository.CorporationRepository;
import com.example.customersupport.repository.FAQRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FAQService {


    private final FAQRepository faqRepository;

    private final CorporationRepository corporationRepository;

    public FAQService(FAQRepository faqRepository, CorporationRepository corporationRepository) {
        this.faqRepository = faqRepository;
        this.corporationRepository = corporationRepository;
    }

    public FAQ createFAQ(String corpName, FAQ faq) {
        Optional<Corporation> corporation = corporationRepository.findByCorpName(corpName);
        if (corporation.isPresent()) {
            faq.setCorporation(corporation.get());
            return faqRepository.save(faq);
        } else {
            throw new IllegalArgumentException("Corporation not found with name: " + corpName);
        }
    }

    public List<FAQ> createFAQs(String corpName, List<FAQ> faqs) {
        Optional<Corporation> corporation = corporationRepository.findByCorpName(corpName);
        if (corporation.isPresent()) {
            Corporation corp = corporation.get();
            faqs.forEach(faq -> faq.setCorporation(corp));
            return faqRepository.saveAll(faqs);
        } else {
            throw new IllegalArgumentException("Corporation not found with name: " + corpName);
        }
    }

    public FAQ updateFAQ(Long faqId, FAQ updatedFaq) {
        Optional<FAQ> existingFaq = faqRepository.findById(faqId);
        if (existingFaq.isPresent()) {
            FAQ faq = existingFaq.get();
            faq.setQuestion(updatedFaq.getQuestion());
            faq.setAnswer(updatedFaq.getAnswer());
            return faqRepository.save(faq);
        } else {
            throw new IllegalArgumentException("FAQ not found with id: " + faqId);
        }
    }

    public void deleteFAQ(Long faqId) {
        faqRepository.deleteById(faqId);
    }

    public List<FAQ> getFAQsByCorporation(String corpName) {
        Optional<Corporation> corporation = corporationRepository.findByCorpName(corpName);
        if (corporation.isPresent()) {
            return faqRepository.findByCorporationId(corporation.get().getId());
        } else {
            throw new IllegalArgumentException("Corporation not found with name: " + corpName);
        }
    }

    public String getFAQsAsString(String corpName) {
        List<FAQ> faqs = getFAQsByCorporation(corpName);

        if (faqs.isEmpty()) {
            return "No FAQs available for corporation: " + corpName;
        }

        return faqs.stream()
                .map(faq -> "Q: " + faq.getQuestion() + "\nA: " + faq.getAnswer())
                .collect(Collectors.joining("\n\n"));  // Join FAQs with a new line between each Q&A
    }
}