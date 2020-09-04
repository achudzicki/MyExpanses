package com.chudzick.expanses.config;

import com.chudzick.expanses.util.jwt.JwtTokenUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@ConfigurationProperties("security")
public class SecurityProperties {

    private String domain;
    private String authLoginUrl;
    private String jwtSecret;
    private String tokenType;
    private String accessTokenTime;
    private String refreshTokenTime;
    private String jwtTokenPrefix;
    private String platformKey;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getAuthLoginUrl() {
        return authLoginUrl;
    }

    public void setAuthLoginUrl(String authLoginUrl) {
        this.authLoginUrl = authLoginUrl;
    }

    public String getJwtSecret() {
        return jwtSecret;
    }

    public void setJwtSecret(String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getAccessTokenTime() {
        return accessTokenTime;
    }

    public void setAccessTokenTime(String accessTokenTime) {
        this.accessTokenTime = accessTokenTime;
    }

    public String getRefreshTokenTime() {
        return refreshTokenTime;
    }

    public void setRefreshTokenTime(String refreshTokenTime) {
        this.refreshTokenTime = refreshTokenTime;
    }

    public String getJwtTokenPrefix() {
        return jwtTokenPrefix;
    }

    public void setJwtTokenPrefix(String jwtTokenPrefix) {
        this.jwtTokenPrefix = jwtTokenPrefix;
    }

    public String getPlatformKey() {
        return platformKey;
    }

    public void setPlatformKey(String platformKey) {
        this.platformKey = platformKey;
    }

    @PostConstruct
    public void tokenGenerator() {
        JwtTokenUtils.setProperties(this);
    }
}
