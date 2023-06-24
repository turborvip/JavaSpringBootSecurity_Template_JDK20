package com.turborvip.security.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.turborvip.security.application.constants.CommonConstant;
import com.turborvip.security.domain.entity.base.AbstractBase;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User extends AbstractBase implements UserDetails {

    @Column(name = "fullname")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String fullName;

    @NotEmpty(message = "Username must not be empty")
//    @Size(min = 6, max = 20, message = "Username must be between 6 and 20 characters")
//    @Column(name = "username", nullable = false, unique = true)
    private String username;


    @NotEmpty(message = "Password must not be empty")
//    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+-=\\[\\]{};:'\"<>,.?/]).{8,}$",
//            message = "Password is not valid")
    @Column(name = "password", nullable = false)
    private String password;

    //    @Pattern(regexp = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$",
//            message = "Email is not format")
//    @Column(name = "email", nullable = false)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String email;

    @JsonFormat(pattern = CommonConstant.FORMAT_DATE_PATTERN)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "birthday")
    private Date birthday;

    @Column(name = "gender")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String gender;
    //    @Pattern(regexp = "^\\d{10}$", message = "Phone number must be 10 characters")
    @Column(name = "phone")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String phone;

    @Column(name = "address")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String address;

    @Column(name = "avatar")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String avatar;


    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @Override
    public int hashCode() {
        return Objects.hash(fullName, username, password, email, birthday, gender, phone, address, avatar);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        roles.stream().forEach(i -> authorities.add(new SimpleGrantedAuthority(i.getRoleName().name())));
        return List.of(new SimpleGrantedAuthority(authorities.toString()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
