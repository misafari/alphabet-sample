package com.alefba.sample.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record SignInRequest(
        @NotBlank(message = "username is required")
        String username,
        @NotBlank(message = "password is required")
        String password
) {
}
