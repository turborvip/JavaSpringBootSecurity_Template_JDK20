package com.turborvip.security.domain.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.turborvip.security.domain.entity.base.AbstractBase;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import java.sql.Timestamp;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tokens",schema = "token")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Token extends AbstractBase {
    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @NotEmpty(message = "Type must not be empty")
    @Column(name = "type")
    private String type;

    @Column(name = "value",columnDefinition="TEXT")
    private String value;

    @NotEmpty(message = "Verify key must not be empty")
    @Column(name="verify_key",columnDefinition="TEXT")
    private  String verifyKey;

    @NotEmpty(message = "Expire time must not be empty")
    @Column(name = "expires_at")
    private Timestamp expiresAt;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "user_device_id")
    private UserDevice userDevice;
}
