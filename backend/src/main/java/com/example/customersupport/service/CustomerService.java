package com.example.customersupport.service;

import com.example.customersupport.dto.CustomerRequestDTO;
import com.example.customersupport.model.relational.Customer;
import com.example.customersupport.repository.relational.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    // Method using Entity
    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    // Method returning a DTO
    public List<CustomerRequestDTO> getAllCustomerDTOs() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream()
                .map(customer -> new CustomerRequestDTO(customer.getName(), customer.getEmail(), customer.getContactNumber()))
                .collect(Collectors.toList());
    }

    // Find customer by email
    public Customer getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email);
    }



    //Method that converts DTO to entity before processing
    public Customer createCustomerFromDTO(CustomerRequestDTO customerRequestDTO) {
        Customer customer = new Customer();
        customer.setName(customerRequestDTO.getName());
        customer.setEmail(customerRequestDTO.getEmail());
        return customerRepository.save(customer);
    }
}