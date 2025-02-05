package com.kinder.kinder_ielts.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kinder.kinder_ielts.constant.AccountStatus;
import com.kinder.kinder_ielts.constant.Role;
import com.kinder.kinder_ielts.entity.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Entity(name = "accounts")
public class Account extends BaseEntity implements UserDetails {
    @Id
    @Size(max = 255)
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "username", length = 30)
    private String username;

    @Size(max = 255)
    @Nationalized
    @Column(name = "password")
    private String password;

    @Lob
    @Column(name = "avatar")
    private String avatar;

    @Column(name = "role", length = 10)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "status", length = 10)
    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @OneToOne(mappedBy = "account", fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    private Tutor tutor;

    @OneToOne(mappedBy = "account", fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    private Student student;

    @OneToMany(mappedBy = "account", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<AccountToken> accountTokens;

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role.name()));
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

    public Account(String id) {
        this.id = id;
    }

    public Account() {
    }
}