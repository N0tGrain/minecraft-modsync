package com.n0tgrain.modsyncbackend.config;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.n0tgrain.modsyncbackend.models.CustomUser;
import com.n0tgrain.modsyncbackend.repositories.CustomUserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final CustomUserRepository customUserRepository;

    public JWTFilter(JWTService jwtService, CustomUserRepository customUserRepository) {
        this.jwtService = jwtService;
        this.customUserRepository = customUserRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || authHeader.isBlank() || !authHeader.regionMatches(true, 0, "Bearer ", 0, 7)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7).trim();
        try {
            String username = jwtService.validateTokenAndRetrieveSubject(token);
            CustomUser user = customUserRepository.findByUsername(username).orElseThrow();

            String authority = normalizeRole(user.getRole().getRoleName());
            var authorities = List.of(new SimpleGrantedAuthority(authority));

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    authorities
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        filterChain.doFilter(request, response);
    }

    private String normalizeRole(String roleName) {
        if (roleName == null || roleName.isBlank()) {
            throw new IllegalArgumentException("User role cannot be null or empty");
        }
        return roleName.startsWith("ROLE_") ? roleName : "ROLE_" + roleName;
    }
}
