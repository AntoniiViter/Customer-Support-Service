package com.example.customersupport.service;

import com.example.customersupport.model.auth.AuthCorporationDetails;
import com.example.customersupport.repository.CorporationRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class JpaCorporationDetailsService implements UserDetailsService {

    private final CorporationRepository corporationRepository;

    public JpaCorporationDetailsService(CorporationRepository corporationRepository) {
        this.corporationRepository = corporationRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return corporationRepository
                .findByEmail(email)
                .map(AuthCorporationDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("Email not found: " + email));
    }
}
