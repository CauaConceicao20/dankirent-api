package com.dankirent.api.controller.auth;

import com.dankirent.api.model.auth.LoginRequestDto;
import com.dankirent.api.model.auth.LoginResponseDto;
import com.dankirent.api.model.user.User;
import com.dankirent.api.model.user.dto.UserRequestDto;
import com.dankirent.api.model.user.dto.UserResponseDto;
import com.dankirent.api.service.AuthService;
import com.dankirent.api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("v1/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto body) {
        String token = authService.authenticate(body);
        return ResponseEntity.ok(new LoginResponseDto(token));
    }

    @PostMapping("v1/register")
    public ResponseEntity<UserResponseDto> register(@RequestBody @Valid UserRequestDto body) {
        User user = userService.create(new User(body));
        return ResponseEntity.ok(new UserResponseDto(user));
    }
}
