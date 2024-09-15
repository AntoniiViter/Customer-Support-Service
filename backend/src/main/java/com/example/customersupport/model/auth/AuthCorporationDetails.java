package com.example.customersupport.model.auth;

import com.example.customersupport.model.relational.Corporation;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;


public class AuthCorporationDetails implements UserDetails {

    private final Corporation corporation;

    public AuthCorporationDetails(Corporation corporation) {
        this.corporation = corporation;
    }

    public String getCorpName() {
        return corporation.getCorpName();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return corporation.getAuthorities();
    }

    @Override
    public String getPassword() {
        return corporation.getPassword();
    }

    @Override
    public String getUsername() {
        return corporation.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
