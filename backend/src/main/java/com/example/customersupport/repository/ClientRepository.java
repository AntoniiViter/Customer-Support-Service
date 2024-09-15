package com.example.customersupport.repository;

import com.example.customersupport.model.relational.Client;
import com.example.customersupport.model.relational.Corporation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByEmailAndCorporation_CorpName(String email, String corpName);
    Optional<Client> findByEmailAndCorporation(String email, Corporation corporation);
    List<Client> findByCorporation(Corporation corporation);
    Optional<Client> findByEmail(String email);
    Optional<Client> findByConversationIdsContaining(String conversationId);


}
