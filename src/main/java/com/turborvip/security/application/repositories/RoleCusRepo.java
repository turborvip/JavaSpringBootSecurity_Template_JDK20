package com.turborvip.security.application.repositories;

import com.turborvip.security.application.constants.EnumRole;
import com.turborvip.security.domain.entity.Role;
import com.turborvip.security.domain.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.EnumType;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleCusRepo {
//    @PersistenceContext
//    private EntityManager entityManager;

     List<Role> getRole(User user);
    
}
