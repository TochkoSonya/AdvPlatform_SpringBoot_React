package com.tochko.advertising_platform.repository;

import com.tochko.advertising_platform.model.enums.ERole;
import com.tochko.advertising_platform.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(ERole name);
    Role findRoleByName(ERole role);
}

