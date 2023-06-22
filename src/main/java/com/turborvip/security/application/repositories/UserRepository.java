package com.turborvip.security.application.responsitories;

import com.turborvip.security.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Boolean existsByUsername(String email);

    User findByUsername(String username);
}
