package com.turborvip.security.application.repositories;

import com.turborvip.security.domain.entity.UserDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Optional;

public interface UserDeviceRepository extends JpaRepository<UserDevice, Long> {
    Optional<UserDevice>  findFirstByCreateBy_IdAndDeviceID(Long id, String deviceID);

    @Transactional
    @Modifying
    @Query("update UserDevice u set u.lastLoginAt = :lastLoginAt where u.id = :id")
    int updateLastLoginAtById(@Param("lastLoginAt") Timestamp lastLoginAt, @Param("id") Long id);

}
