package com.apirest.JwtAuthService.persistence.repository;

import com.apirest.JwtAuthService.persistence.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    List<Role> findByRoleEnumIn(List<String> roleNames);
}
