package com.turborvip.security.domain.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;


@Getter
@Setter
public class UserDTO {
    @NotBlank(message = "FullName is not blank")
    private String fullName;
    @NotBlank(message = "Username is not blank")
    private String username;
    @NotBlank(message = "Password is not blank")
    private String password;
    private String email;
    private String birthday;
    private String phone;
    private String gender;
    private String address;
    private String avatar;
}
