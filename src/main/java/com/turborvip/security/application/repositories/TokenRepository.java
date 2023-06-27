package com.turborvip.security.application.repositories;

import com.turborvip.security.domain.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token,Long> {
    Optional<Token> findFirstByCreateBy_IdAndTypeAndUserDevice_DeviceID(Long id, String type, String deviceID);

    @Override
    void deleteById(Long id);

    @Transactional
    @Modifying
    @Query("update Token t set t.updateAt = :updateAt, t.value = :value, t.expiresAt = :expiresAt where t.id = :id")
    int updateUpdateAtAndValueAndExpiresAtById(@Param("updateAt") Timestamp updateAt, @Param("value") String value, @Param("expiresAt") Timestamp expiresAt, @Param("id") Long id);

    List<Token> findByCreateBy_IdAndUserDevice_DeviceID(Long id, String deviceID);

    Optional<Token> findFirstByValue(String value);

    List<Token> findByExpiresAtLessThan(Timestamp expiresAt);





}
