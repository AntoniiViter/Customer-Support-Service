package com.example.customersupport.controller;

import com.example.customersupport.model.relational.Client;
import com.example.customersupport.service.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/{corpName}/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    // POST: Create a new Client
    @PostMapping
    public ResponseEntity<Client> createClient(@RequestBody Client client) {
        Client createdClient = clientService.createClient(client);
        return ResponseEntity.ok(createdClient);
    }

    // GET: Get a Client by ID
    @GetMapping("/{clientId}")
    public ResponseEntity<Client> getClientById(@PathVariable Long clientId) {
        return clientService.getClientById(clientId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new IllegalArgumentException("Client not found with ID: " + clientId));
    }

    // PUT: Update an existing Client by ID
    @PutMapping("/{clientId}")
    public ResponseEntity<Client> updateClient(@PathVariable Long clientId, @RequestBody Client client) {
        Client updatedClient = clientService.updateClient(clientId, client);
        return ResponseEntity.ok(updatedClient);
    }

    // DELETE: Delete a Client by ID
    @DeleteMapping("/{clientId}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long clientId) {
        clientService.deleteClient(clientId);
        return ResponseEntity.noContent().build();
    }
}
