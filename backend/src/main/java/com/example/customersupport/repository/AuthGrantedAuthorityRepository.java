package com.example.customersupport.repository;

import com.example.customersupport.model.auth.AuthGrantedAuthority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthGrantedAuthorityRepository extends JpaRepository<AuthGrantedAuthority, Long> {
}
