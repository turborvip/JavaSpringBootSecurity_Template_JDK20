package com.turborvip.security.application.repositories;

import com.turborvip.security.domain.entity.Token;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findFirstByCreateBy_IdAndTypeAndUserDevice_DeviceID(Long id, String type, String deviceID);

    @Override
    void deleteById(Long id);

    List<Token> findByCreateBy_IdAndUserDevice_DeviceID(Long id, String deviceID);

    Optional<Token> findFirstByValue(String value);

    List<Token> findByExpiresAtLessThan(Timestamp expiresAt);

    Optional<Token> findFirstByValueAndType(String value, String type);

    @Transactional
    @Query(value = "select * from token.tokens where :refreshToken = ANY(token.tokens.refresh_token_used)",nativeQuery = true)
    Optional<Token> findFirstByRefreshTokenUsedContains(@Param("refreshToken") String refreshToken);

//    Optional<Token> findFirstByRefreshTokenUsedContains(String refreshTokenUsed);



    List<Token> findByCreateBy_Id(Long id);



}
