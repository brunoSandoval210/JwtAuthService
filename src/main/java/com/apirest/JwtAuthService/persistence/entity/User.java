package com.apirest.JwtAuthService.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true)
    private String username;

    private String password;

    @Column(name = "is_enabled")
    private boolean isEnabled;// true si el usuario esta habilitado, false si no lo esta

    @Column(name = "account_no_expired")
    private boolean accountNoExpired;// true si la cuenta no ha expirado, false si ha expirado

    @Column(name = "account_no_locked")
    private boolean accountNonLocked;// true si la cuenta no esta bloqueada, false si esta bloqueada

    @Column(name = "credentials_no_expired")
    private boolean credentialsNonExpired;// true si las credenciales no han expirado, false si han expirado

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)// EAGER para que se carguen los roles al cargar el usuario y cascade ALL para que se guarden los roles al guardar el usuario
    @JoinTable(name = "user_roles",
                joinColumns = @JoinColumn(name = "user_id"),
                inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
}
