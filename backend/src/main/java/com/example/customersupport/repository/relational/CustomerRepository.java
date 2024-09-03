package com.example.customersupport.repository.relational;

import com.example.customersupport.model.relational.Corporation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Corporation, Long> {
    Corporation findByEmail(String email);
}