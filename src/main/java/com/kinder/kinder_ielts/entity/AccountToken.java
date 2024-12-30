package com.kinder.kinder_ielts.entity;

import com.kinder.kinder_ielts.constant.IsRevoked;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "account_token")
public class AccountToken {

    @Id
    @Size(max = 255)
    @Column(name = "id", nullable = false)
    private String id;

    @NotNull
    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "is_revoked", nullable = false)
    private IsRevoked isRevoked = IsRevoked.NOT_REVOKED;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

}