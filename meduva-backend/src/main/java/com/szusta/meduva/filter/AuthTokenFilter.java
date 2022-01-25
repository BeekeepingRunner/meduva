package com.szusta.meduva.filter;

import com.szusta.meduva.security.jwt.JwtUtils;
import com.szusta.meduva.service.user.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// Executes once per request to authenticate and authorize user,
// and to set his UserDetails in Security Context.
public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            setUserAuthentication(request);
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }
        filterChain.doFilter(request, response);
    }

    private void setUserAuthentication(HttpServletRequest request) {
        String jwt = parseJwtFrom(request);
        if (jwt != null && jwtUtils.validateJwtToken(jwt)) {

            UsernamePasswordAuthenticationToken authentication = createAuthenticationFrom(request, jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

    private String parseJwtFrom(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");

        if (hasPrefix(authHeader)) {
            return stripOffPrefix(authHeader);
        } else {
            return null;
        }
    }

    private boolean hasPrefix(String authHeader) {
        return StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ");
    }

    private String stripOffPrefix(String authHeader) {
        return authHeader.substring(7, authHeader.length());
    }

    private UsernamePasswordAuthenticationToken createAuthenticationFrom(
            HttpServletRequest request, String jwt) {

        String username = jwtUtils.getUserNameFromJwt(jwt);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return authentication;
    }
}
