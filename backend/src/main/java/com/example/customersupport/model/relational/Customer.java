package com.example.customersupport.model.relational;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String contactNumber; // New field added

    // Constructors, Getters, and Setters
    public Customer() {}

    public Customer(String name, String email, String contactNumber) { // Updated constructor
        this.name = name;
        this.email = email;
        this.contactNumber = contactNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNumber() { // New getter
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) { // New setter
        this.contactNumber = contactNumber;
    }
}