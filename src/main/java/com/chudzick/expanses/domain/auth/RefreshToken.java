package com.chudzick.expanses.domain.auth;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "USER_REFRESH_TOKENS")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "refresh_token_generator")
    @SequenceGenerator(name = "refresh_token_generator", sequenceName = "refresh_token_seq")
    @NotNull(message = "ID can not be null")
    @Column(nullable = false)
    private Long id;
    @NotNull(message = "Email can not be null")
    private String email;
    @Column(unique = true, columnDefinition = "TEXT")
    private String token;
    private boolean invalid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isInvalid() {
        return invalid;
    }

    public void setInvalid(boolean invalid) {
        this.invalid = invalid;
    }
}
