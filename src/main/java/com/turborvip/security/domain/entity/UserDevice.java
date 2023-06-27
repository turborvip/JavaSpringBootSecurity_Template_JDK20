package com.turborvip.security.domain.entity;

import com.turborvip.security.domain.entity.base.AbstractBase;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_device")
public class UserDevice extends AbstractBase {

    @NotEmpty(message = "Device id must not be empty")
    @Column(name = "device_id")
    private String deviceID;

    @Column(name = "status")
    private String status = "active";

    @NotEmpty(message = "Last login at must not be empty")
    @Column(name = "last_login_at")
    private Timestamp lastLoginAt;

    @Column(name = "locked_at")
    private Timestamp lockedAt;

    @Column(name = "locked_until")
    private Timestamp lockedUntil;

    @Column(name = "unlocked_at")
    private Timestamp unlockedAt;

    public UserDevice( String deviceID, Timestamp lastLoginAt, Timestamp lockedAt, Timestamp lockedUntil, Timestamp unlockedAt) {
        this.deviceID = deviceID;
        this.lastLoginAt = lastLoginAt;
        this.lockedAt = lockedAt;
        this.lockedUntil = lockedUntil;
        this.unlockedAt = unlockedAt;
    }
}
