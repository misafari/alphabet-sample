package com.alefba.sample.controller.dto;

import java.util.List;

public record SignInResponse(
        String token,
        String username,
        List<String> roles
) {
}
