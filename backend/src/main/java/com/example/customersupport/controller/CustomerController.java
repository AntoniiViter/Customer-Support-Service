package com.example.customersupport.controller;

import com.example.customersupport.dto.CustomerRequestDTO;
import com.example.customersupport.model.relational.Customer;
import com.example.customersupport.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    // Reference to DTO for incoming data
    @PostMapping
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody CustomerRequestDTO customerRequestDTO) {
        // Convert DTO to Entity
        Customer customer = new Customer();
        customer.setName(customerRequestDTO.getName());
        customer.setEmail(customerRequestDTO.getEmail());
        customer.setContactNumber(customerRequestDTO.getContactNumber());

        Customer savedCustomer = customerService.saveCustomer(customer);
        return ResponseEntity.ok(savedCustomer);
    }

    // Reference to DTO for outgoing data
    @GetMapping
    public ResponseEntity<List<CustomerRequestDTO>> getAllCustomers() {
        List<CustomerRequestDTO> customers = customerService.getAllCustomerDTOs();
        List<CustomerRequestDTO> customerDTOs = customers.stream()
                .map(customer -> new CustomerRequestDTO(customer.getName(), customer.getEmail(), customer.getContactNumber()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(customerDTOs);
    }

    // Find customers by email
    @GetMapping("/search")
    public ResponseEntity<Customer> getCustomersByEmail(@RequestParam String email) {
        Customer customers = customerService.getCustomerByEmail(email);
        return ResponseEntity.ok(customers);
    }
}