package com.example.customersupport.repository.relational;

import com.example.customersupport.model.relational.Customer;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByEmail(String email);
}