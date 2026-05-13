package com.assignment.hardship.service;

import com.assignment.hardship.dto.AuthDto;
import com.assignment.hardship.entity.User;
import com.assignment.hardship.enums.Role;
import com.assignment.hardship.repo.UserRepository;
import com.assignment.hardship.util.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final CookieUtil cookieUtil;
    private final AuthenticationManager authenticationManager;

    @Value("${app.jwt.refresh-cookie-name}")
    private String refreshCookieName;

    // --register--
    @Transactional
    public AuthDto.AuthResponse register(AuthDto.RegisterRequest request, HttpServletResponse response) {
        if (userRepository.existsByUsername(request.useName())) {
            throw new IllegalArgumentException("Username already existed");
        }
        Role role = request.role() != null ? request.role() : Role.ROLE_USER;
        User user = User.builder()
                .username(request.useName())
                .password(passwordEncoder.encode(request.password()))
                .role(role)
                .build();

        userRepository.save(user);
        issueTokens(user, response);

        return new AuthDto.AuthResponse(user.getUsername(), user.getRole().name(), "Registered successfully");
    }

    // --login--
    public AuthDto.AuthResponse login(AuthDto.LoginRequest request, HttpServletResponse response) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.useName(), request.password())
        );

        User user = userRepository.findByUsername(request.useName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        issueTokens(user, response);
        return new AuthDto.AuthResponse(request.useName(), user.getRole().name(), "Login successful");
    }

    // --logout--
    public void logout(HttpServletResponse response) {
        cookieUtil.clearCookies(response);
    }

    // --refresh--
    public AuthDto.AuthResponse refresh(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = extractRefreshToken(request);
        if (refreshToken == null) {
            throw new IllegalArgumentException("Refresh token not found");
        }

        String username = jwtService.extractUserName(refreshToken);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!jwtService.isTokenValid(refreshToken, user)) {
            throw new IllegalArgumentException("Invalid or expired refresh token");
        }

        issueTokens(user, response);
        return new AuthDto.AuthResponse(username, user.getRole().name(), "Token Refresh");
    }

    // --helpers

    public void issueTokens(User user, HttpServletResponse response) {
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        cookieUtil.addAccessTokenCookie(response, accessToken);
        cookieUtil.addRefreshTokenCookie(response, refreshToken);
    }

    public String extractRefreshToken(HttpServletRequest request) {
        if (request.getCookies() == null) return null;
        return Arrays.stream(request.getCookies())
                .filter((c) -> refreshCookieName.equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
}
