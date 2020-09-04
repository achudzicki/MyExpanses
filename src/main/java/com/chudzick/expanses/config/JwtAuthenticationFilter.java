package com.chudzick.expanses.config;

import com.chudzick.expanses.domain.auth.*;
import com.chudzick.expanses.services.auth.AuthRefreshTokenService;
import com.chudzick.expanses.services.users.UserDetailServiceImpl;
import com.chudzick.expanses.util.jwt.JwtTokenUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.security.auth.message.AuthException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private static final Logger LOG = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final AuthRefreshTokenService authRefreshTokenService;
    private final UserDetailServiceImpl userDetailService;
    private final AuthenticationManager authenticationManager;

    JwtAuthenticationFilter(AuthRefreshTokenService authRefreshTokenService, UserDetailServiceImpl userDetailService,
                            AuthenticationManager authenticationManager,
                            SecurityProperties properties) {
        this.authRefreshTokenService = authRefreshTokenService;
        this.userDetailService = userDetailService;
        this.authenticationManager = authenticationManager;
        setFilterProcessesUrl(properties.getAuthLoginUrl());
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            AuthRequest authRequest = new ObjectMapper()
                    .readValue(request.getInputStream(), AuthRequest.class);
            if (authRequest.getGrantType() == null || authRequest.getGrantType().isEmpty()) {
                LOG.error(String.format("Try to auth with wrong grantType '%s'", authRequest.getGrantType()));
                throw new AuthException("Null or empty grantType passed");
            } else if (authRequest.getGrantType().equals(GrantType.USERNAME_PASSWORD.getType())) {
                LOG.info("Try to auth with username and password");
                return authenticateByUsernamePassword(authRequest);
            } else if (authRequest.getGrantType().equals(GrantType.REFRESH_TOKEN.getType())) {
                LOG.info("Try to auth with refresh token");
                String refreshToken = authRefreshTokenService.extractRefreshTokenFromCookies(request.getCookies());
                return authByRefreshToken(authRequest.getUsername(), refreshToken);
            }
            LOG.error("Invalid grand type passed to auth request");
            throw new AuthException("Wrong grantType passed");
        } catch (IOException | AuthException e) {
            LOG.error(e.getMessage(), e);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            throw new BadCredentialsException("Wrong username or password");
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        AuthUserDetails details = (AuthUserDetails) authResult.getDetails();
        JwtResponse jwtResponse = JwtTokenUtils.generateJwtTokenResponse(details);
        RefreshToken refreshToken = JwtTokenUtils.generateJwtRefreshToken(details.getUsername());
        authRefreshTokenService.save(refreshToken);
        response.addCookie(authRefreshTokenService.prepareCookie(refreshToken));

        LOG.info("Successfully authenticate for user {}", details.getUsername());
        response.setStatus(HttpStatus.OK.value());
        sendResponse(response, jwtResponse);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        LOG.warn("Unsuccessful authentication - {}", failed.getMessage(), failed);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        sendResponse(response, failed.getMessage());
    }

    private Authentication authByRefreshToken(String username, String refreshToken) throws AuthException {
        LOG.info("Start to authenticate by refresh token");
        UserDetails userDetails = userDetailService.loadUserByUsername(username);
        if (StringUtils.isEmpty(refreshToken) || !authRefreshTokenService.checkRefreshTokenAndSetInvalid(username, refreshToken)) {
            LOG.warn("Refresh token is empty or invalid");
            throw new AuthException("Refresh token is null, empty or not valid");
        }
        UsernamePasswordAuthenticationToken authenticationToken = JwtTokenUtils
                .validateJwtRefreshToken(refreshToken, userDetails);
        authenticationToken.setDetails(userDetails);
        return authenticationToken;
    }

    private Authentication authenticateByUsernamePassword(AuthRequest authRequest) {
        LOG.info("Start to authenticate by username and password");
        UserDetails userDetails = userDetailService.loadUserByUsername(authRequest.getUsername());
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword());
        authenticationToken.setDetails(userDetails);
        return authenticationManager.authenticate(authenticationToken);
    }

    private void sendResponse(HttpServletResponse httpResponse, Object response) throws IOException {
        JSONObject jsonObject = new JSONObject(response);
        PrintWriter writer = httpResponse.getWriter();
        httpResponse.setContentType("application/json");
        writer.write(jsonObject.toString());
        writer.flush();
        writer.close();
    }
}
