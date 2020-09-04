package com.chudzick.expanses.services.auth;

import com.chudzick.expanses.domain.auth.RefreshToken;
import com.chudzick.expanses.repositories.RefreshTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.message.AuthException;
import javax.servlet.http.Cookie;
import java.util.Optional;

@Service
public class AuthRefreshTokenServiceImpl implements AuthRefreshTokenService {
    private static final Logger LOG = LoggerFactory.getLogger(AuthRefreshTokenServiceImpl.class);
    private static final String COOKIE_REFRESH_TOKEN_NAME = "refreshToken";

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Override
    public String extractRefreshTokenFromCookies(Cookie[] cookies) throws AuthException {
        if (cookies == null || cookies.length == 0) {
            LOG.warn("Request cookies are null or empty");
            throw new AuthException("No refresh token from cookies");
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(COOKIE_REFRESH_TOKEN_NAME)) {
                return cookie.getValue();
            }
        }

        LOG.warn("Try to extract refresh token from cookies but cookie not found in request");
        return null;
    }

    @Override
    @Transactional
    public void save(RefreshToken refreshToken) {
        refreshTokenRepository.save(refreshToken);
    }

    @Override
    public Cookie prepareCookie(RefreshToken refreshToken) {
        LOG.info("Prepare refreshToken cookie");
        Cookie refreshTokenCookie =
                new Cookie(COOKIE_REFRESH_TOKEN_NAME, refreshToken.getToken());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        return refreshTokenCookie;
    }

    @Override
    @Transactional
    public boolean checkRefreshTokenAndSetInvalid(String username, String refreshToken) {
        LOG.info("Check if refresh token is valid");
        Optional<RefreshToken> foundRefreshToken = refreshTokenRepository.findByToken(refreshToken);
        if (foundRefreshToken.isPresent()) {
            RefreshToken dbRefreshToken = foundRefreshToken.get();
            boolean isValid = dbRefreshToken.getEmail().equals(username) && !dbRefreshToken.isInvalid();
            dbRefreshToken.setInvalid(true);
            save(dbRefreshToken);
            LOG.info("Refresh token valid - {}", isValid);
            return isValid;
        }
        return false;
    }
}
