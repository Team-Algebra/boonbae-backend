package com.agebra.boonbaebackend.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "refersh_token")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    @NotBlank
    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "login_id")
    private String loginId;

    public RefreshToken updateToken(String token) {
        this.refreshToken = token;
        return this;
    }
}
