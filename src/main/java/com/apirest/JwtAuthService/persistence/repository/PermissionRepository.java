package com.apirest.JwtAuthService.persistence.repository;

import com.apirest.JwtAuthService.persistence.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission,Long> {
}
