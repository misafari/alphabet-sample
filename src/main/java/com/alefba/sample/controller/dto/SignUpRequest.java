package com.alefba.sample.controller.dto;

import com.alefba.sample.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignUpRequest(
        @NotBlank
        @Size(min = 3, max = 190, message = "Username must longer than 3 character")
        String username,

        @NotBlank
        @Size(min = 5, message = "Password must contain at least 5 characters")
        String password
) {
    public User to() {
        var user = new User();

        user.setUsername(username);
        user.setPassword(password);

        return user;
    }
}
