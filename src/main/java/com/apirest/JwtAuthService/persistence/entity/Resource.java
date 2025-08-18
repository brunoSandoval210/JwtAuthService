package com.apirest.JwtAuthService.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "resources")
public class Resource extends Maintenance implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long resourceId;

    @Column(unique = true, nullable = false)
    private String name;

    @Size(max = 100)
    private String description;

    @ToString.Exclude
    @OneToMany(mappedBy = "resource", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Permission> permissions = new ArrayList<>();
}
