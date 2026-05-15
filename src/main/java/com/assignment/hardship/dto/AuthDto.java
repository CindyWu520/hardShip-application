package com.assignment.hardship.dto;

import com.assignment.hardship.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public final class AuthDto {
    private AuthDto() {
    }

    public record LoginRequest(
            @NotBlank @Size(max = 64)
            String username,
            String password
    ) {
    }

    public record RegisterRequest(
            @NotBlank @Size(max = 64)
            String username,
            @NotBlank @Size(min = 8, max = 100)
            String password,
            Role role // optional
    ) {
    }

    public record AuthResponse(String username, String role, String message) {
    }

    public record MeResponse(String username, String role) {
    }
}


