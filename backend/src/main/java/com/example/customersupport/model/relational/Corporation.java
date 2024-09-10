package com.example.customersupport.model.relational;

import com.example.customersupport.model.auth.AuthGrantedAuthority;
import lombok.*;

import jakarta.persistence.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "corporations")
public class Corporation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "corp_name", nullable = false, unique = true)
    private String corpName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @OneToMany(mappedBy = "corporation", fetch = FetchType.EAGER)
    private Set<AuthGrantedAuthority> authorities;


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "corporation_id") // Foreign key added to Client table
    private List<Client> clients;


}