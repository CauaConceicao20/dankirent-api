package com.dankirent.api.controller.auth;

import com.dankirent.api.infrastructure.cookie.CookieService;
import com.dankirent.api.infrastructure.security.UserDetailsImpl;
import com.dankirent.api.model.auth.LoginRequestDto;
import com.dankirent.api.model.auth.LoginResponseDto;
import com.dankirent.api.model.user.User;
import com.dankirent.api.model.auth.UserLoggedResponseDto;
import com.dankirent.api.model.user.dto.UserRequestDto;
import com.dankirent.api.model.user.dto.UserResponseDto;
import com.dankirent.api.service.AuthService;
import com.dankirent.api.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController implements AuthControllerDoc {

    private final AuthService authService;
    private final UserService userService;
    private final CookieService cookieService;

    @PostMapping("v1/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto body, HttpServletResponse response) {
        String token = authService.authenticate(body);
        ResponseCookie cookie = cookieService.setAccessTokenInCookie(token, response);
        response.addHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.ok(new LoginResponseDto(token));
    }

    @PostMapping("v1/register")
    public ResponseEntity<UserResponseDto> register(@RequestBody @Valid UserRequestDto body) {
        User user = userService.create(new User(body));
        return ResponseEntity.ok(new UserResponseDto(user));
    }

    @GetMapping("v1/me")
    public ResponseEntity<UserLoggedResponseDto> getMe(@AuthenticationPrincipal UserDetailsImpl userDetails) {
       return ResponseEntity.ok(new UserLoggedResponseDto(userDetails));
    }

    @PostMapping("v1/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        ResponseCookie cookie = cookieService.removeAccessTokenInCookie(response);
        response.addHeader("Set-Cookie", cookie.toString());
        return ResponseEntity.noContent().build();
    }
}
