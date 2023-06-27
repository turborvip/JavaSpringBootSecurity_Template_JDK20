package com.turborvip.security.application.services.impl;

import com.turborvip.security.application.repositories.UserDeviceRepository;
import com.turborvip.security.application.services.UserDeviceService;
import com.turborvip.security.domain.entity.UserDevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

@Service
public class UserDeviceServiceImpl implements UserDeviceService {
    @Autowired
    UserDeviceRepository userDeviceRepository;
    @Override
    public Optional<UserDevice> findDeviceByUserIdAndDeviceId(Long userId,String deviceId) {
        return userDeviceRepository.findFirstByCreateBy_IdAndDeviceID(userId,deviceId);
    }

    @Override
    public UserDevice create(UserDevice userDevice) {
        return userDeviceRepository.save(userDevice);
    }

    @Override
    public int updateLastLogin(Timestamp lastLoginAt, Long userDeviceId) {
        return userDeviceRepository.updateLastLoginAtById(lastLoginAt,userDeviceId);
    }
}
