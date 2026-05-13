package com.assignment.hardship.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {
    @Value("${app.jwt.cookie-name}")
    private String cookieName;
    @Value("${app.jwt.expiration-ms}")
    private int accessExpirationMs;
    @Value("${app.jwt.refresh-cookie-name}")
    private String refreshCookieName;
    @Value("${app.jwt.refresh-expiration-ms}")
    private int refreshExpirationMs;

    public void addAccessTokenCookie(HttpServletResponse response, String token) {
        addCookie(response, cookieName, token, accessExpirationMs / 1000);
    }

    public void addRefreshTokenCookie(HttpServletResponse response, String token) {
        addCookie(response, refreshCookieName, token, refreshExpirationMs / 1000);
    }

    public void clearCookies(HttpServletResponse response) {
        addCookie(response, cookieName, "", 0);
        addCookie(response, refreshCookieName, "", 0);
    }

    private void addCookie(HttpServletResponse response, String name, String value, int maxAgeSeconds) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(maxAgeSeconds);

        response.addHeader("Set-Cookie", String.format("%s=%s; Max-Age=%d; Path=/; HttpOnly; Secure; SameSite=Strict", name, value, maxAgeSeconds));
    }


}
