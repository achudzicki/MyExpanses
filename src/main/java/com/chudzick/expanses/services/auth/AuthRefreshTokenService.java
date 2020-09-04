package com.chudzick.expanses.services.auth;

import com.chudzick.expanses.domain.auth.RefreshToken;

import javax.security.auth.message.AuthException;
import javax.servlet.http.Cookie;

public interface AuthRefreshTokenService {
    String extractRefreshTokenFromCookies(Cookie[] cookies) throws AuthException;

    void save(RefreshToken refreshToken);

    Cookie prepareCookie(RefreshToken refreshToken);

    boolean checkRefreshTokenAndSetInvalid(String username, String refreshToken);
}
