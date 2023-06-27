package com.turborvip.security.application.services;

import com.turborvip.security.domain.entity.UserDevice;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.util.Optional;

public interface UserDeviceService {
    Optional<UserDevice> findDeviceByUserIdAndDeviceId(Long userId,String deviceId);

    UserDevice create(UserDevice userDevice);

    int updateLastLogin(Timestamp lastLoginAt,Long userDeviceId);
}
