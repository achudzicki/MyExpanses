package com.chudzick.expanses.util.jwt;

import com.chudzick.expanses.config.SecurityProperties;
import com.chudzick.expanses.domain.auth.AuthUserDetails;
import com.chudzick.expanses.domain.auth.JwtResponse;
import com.chudzick.expanses.domain.auth.RefreshToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.JwtException;

import javax.security.auth.message.AuthException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class JwtTokenUtils {
    private static String jwtSecret;
    private static String tokenType;
    private static String accessTokenTime;
    private static String refreshTokenTime;
    private static String tokenPrefix;
    private static final String CLAIM_TOKEN_TYP_LABEL = "typ";
    private static final String CLAIM_TOKEN_ROLES_LABEL = "roles";
    private static final String CLAIM_TOKEN_USER_ID_LABEL = "userId";
    private static final String CLAIM_RANDOM_PROTECTION = "random";

    private JwtTokenUtils() {
        throw new IllegalStateException();
    }

    public static UsernamePasswordAuthenticationToken validateJwtAccessToken(String token, UserDetailsService userDetailsService) {
        if (token != null && !token.isEmpty() && token.startsWith(tokenPrefix)) {
            Jws<Claims> parsedToken = Jwts.parser()
                    .setSigningKey(jwtSecret.getBytes())
                    .parseClaimsJws(token.replace(tokenPrefix, ""));
            String tokenUsername = parsedToken.getBody().getSubject();
            List<SimpleGrantedAuthority> roles = ((List<?>) parsedToken.getBody().get(CLAIM_TOKEN_ROLES_LABEL)).stream()
                    .map(a -> new SimpleGrantedAuthority((String) a))
                    .collect(Collectors.toList());
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(tokenUsername,
                    null, roles);
            UserDetails userDetails = userDetailsService.loadUserByUsername(tokenUsername);
            authenticationToken.setDetails(userDetails);
            return authenticationToken;
        }
        return null;
    }

    public static UsernamePasswordAuthenticationToken validateJwtRefreshToken(String refreshToken, UserDetails userDetails) throws AuthException {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(jwtSecret.getBytes())
                    .parseClaimsJws(refreshToken.replace(tokenPrefix, ""))
                    .getBody();
        } catch (JwtException e) {
            throw new AuthException(e.getMessage());
        }
        String tokenUsername = claims.getSubject();
        if (!tokenUsername.equals(userDetails.getUsername())) {
            throw new AuthException("Wrong username passed");
        }
        return new UsernamePasswordAuthenticationToken(tokenUsername, null, userDetails.getAuthorities());
    }

    public static JwtResponse generateJwtTokenResponse(AuthUserDetails details) {
        JwtResponse authResponse = new JwtResponse();
        List<String> authorities = details.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        String accessToken = buildAccessToken(details.getUsername(), details.getUserId(), authorities);
        authResponse.setToken(accessToken);
        authResponse.setTokenType(tokenPrefix);
        return authResponse;
    }

    public static RefreshToken generateJwtRefreshToken(String username) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setEmail(username);
        refreshToken.setToken(buildRefreshToken(username));
        return refreshToken;
    }

    private static String buildAccessToken(String username, long userId, List<String> authorities) {
        return Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()), SignatureAlgorithm.HS512)
                .setHeaderParam(CLAIM_TOKEN_TYP_LABEL, tokenType)
                .setSubject(username)
                .claim(CLAIM_TOKEN_USER_ID_LABEL, userId)
                .claim(CLAIM_TOKEN_ROLES_LABEL, authorities)
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(accessTokenTime) * 1000 * 60))
                .compact();
    }

    private static String buildRefreshToken(String username) {
        return Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()), SignatureAlgorithm.HS512)
                .setHeaderParam(CLAIM_TOKEN_TYP_LABEL, tokenType)
                .setSubject(username)
                .claim(CLAIM_RANDOM_PROTECTION, RandomStringUtils.random(5))
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(refreshTokenTime) * 1000 * 60))
                .compact();
    }

    public static void setProperties(SecurityProperties properties) {
        JwtTokenUtils.jwtSecret = properties.getJwtSecret();
        JwtTokenUtils.tokenType = properties.getTokenType();
        JwtTokenUtils.accessTokenTime = properties.getAccessTokenTime();
        JwtTokenUtils.refreshTokenTime = properties.getRefreshTokenTime();
        JwtTokenUtils.tokenPrefix = properties.getJwtTokenPrefix();
    }
}
