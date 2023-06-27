package com.turborvip.security.application.repositories;

import com.turborvip.security.application.constants.EnumRole;
import com.turborvip.security.domain.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findRoleByRoleName(EnumRole roleName);
}
