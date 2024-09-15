package com.example.customersupport.repository;

import com.example.customersupport.model.relational.FAQ;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FAQRepository extends JpaRepository<FAQ, Long> {
    List<FAQ> findByCorporationId(Long corporationId);
}