package com.chudzick.expanses.config;

import com.chudzick.expanses.domain.auth.AuthUserDetails;
import com.chudzick.expanses.util.jwt.JwtTokenUtils;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.stream.Collectors;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private static final Logger LOG = LoggerFactory.getLogger(JwtAuthorizationFilter.class);
    private static final String HEADER_AUTHORIZATION = "Authorization";
    private UserDetailsService userDetailsService;
    private OrRequestMatcher excludedUrlsMatcher;

    JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserDetailsService userDetailsService,
                           String... excludedUrls) {
        super(authenticationManager);
        this.userDetailsService = userDetailsService;
        this.excludedUrlsMatcher = new OrRequestMatcher(Arrays.stream(excludedUrls)
                .map(AntPathRequestMatcher::new)
                .collect(Collectors.toList()));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            Authentication authorization = getAuthorization(request);
            if (authorization == null && excludedUrlsMatcher.matches(request)) {
                chain.doFilter(request, response);
                return;
            } else if (authorization == null) {
                LOG.warn("Authorization is null");
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
            } else {
                AuthUserDetails details = (AuthUserDetails) authorization.getDetails();
                details.hidePassword();
            }
            SecurityContextHolder.getContext().setAuthentication(authorization);
            chain.doFilter(request, response);
        } catch (JwtException | UsernameNotFoundException e) {
            LOG.error(e.getMessage(), e);
            sendResponse(response, "Bad credentials");
        }
    }

    private Authentication getAuthorization(HttpServletRequest request) {
        String token = request.getHeader(HEADER_AUTHORIZATION);
        return JwtTokenUtils.validateJwtAccessToken(token, userDetailsService);
    }

    private void sendResponse(HttpServletResponse response, String message) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpStatus.FORBIDDEN.value());
        PrintWriter writer = response.getWriter();
        writer.print(message);
    }
}
