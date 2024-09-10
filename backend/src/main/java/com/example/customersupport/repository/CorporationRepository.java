package com.example.customersupport.repository;

import com.example.customersupport.model.relational.Corporation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CorporationRepository extends JpaRepository<Corporation, Long> {

    Optional<Corporation> findByEmail(String email);

    Optional<Corporation> findByCorpName(String corpName);
}
