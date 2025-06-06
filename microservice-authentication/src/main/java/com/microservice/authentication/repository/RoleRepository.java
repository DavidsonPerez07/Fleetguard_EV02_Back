package com.microservice.authentication.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.authentication.entity.Role;



@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{
    Optional<Role>  findByRoleId(Long roleId);
}
