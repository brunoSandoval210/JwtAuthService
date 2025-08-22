package com.apirest.JwtAuthService.persistence.repository;

import com.apirest.JwtAuthService.persistence.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    List<Role> findByRoleIn(List<String> roleNames);
    Optional<Role> findByRole(String roleName);
}
