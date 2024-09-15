package com.example.customersupport.model.relational;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "clients",
        uniqueConstraints = @UniqueConstraint(columnNames = {"corporation_id", "email"})
)
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @ElementCollection
    @CollectionTable(name = "client_conversations", joinColumns = @JoinColumn(name = "client_id"), uniqueConstraints = @UniqueConstraint(columnNames = {"client_id", "conversation_id"}))
    @Column(name = "conversation_id")
    private List<String> conversationIds;

    @ManyToOne
    @JoinColumn(name = "corporation_id", nullable = false)
    private Corporation corporation;
}
