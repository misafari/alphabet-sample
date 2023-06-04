package com.alefba.sample.controller;

import com.alefba.sample.controller.dto.SignInRequest;
import com.alefba.sample.controller.dto.SignInResponse;
import com.alefba.sample.controller.dto.SignUpRequest;
import com.alefba.sample.model.User;
import com.alefba.sample.service.UserService;
import com.alefba.sample.util.config.JwtTokenUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping(UserController.ROOT_PATH)
@RequiredArgsConstructor
public class UserController {
    public static final String ROOT_PATH = "/api/v1/auth";
    private final UserService service;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody @Valid SignUpRequest request) {
        service.save(request.to());
    }

    @PostMapping("/sign-in")
    public ResponseEntity<SignInResponse> login(@RequestBody @Valid SignInRequest request) {
        Authentication authenticate = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));

        var user = (User) authenticate.getPrincipal();

        var response = new SignInResponse(
                jwtTokenUtil.generateAccessToken(user.getUsername()),
                user.getUsername(),
                user.getRoles().stream().map(e -> e.getName().name()).collect(Collectors.toList())
        );

        return ResponseEntity.accepted().body(response);
    }
}
