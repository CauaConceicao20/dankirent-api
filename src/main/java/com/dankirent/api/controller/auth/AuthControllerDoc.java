package com.dankirent.api.controller.auth;

import com.dankirent.api.config.SecurityConfig;
import com.dankirent.api.model.auth.LoginRequestDto;
import com.dankirent.api.model.auth.LoginResponseDto;
import com.dankirent.api.model.user.dto.UserRequestDto;
import com.dankirent.api.model.user.dto.UserResponseDto;
import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Auth")
@SecurityRequirement(name = SecurityConfig.SECURITY)
public interface AuthControllerDoc {

    @Operation(summary = "User authentication")
    @ApiResponse(responseCode = "200", description = "User authenticated successfully")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto body);

    @Operation(summary = "User registration")
    @ApiResponse(responseCode = "201", description = "User registered successfully")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "409", description = "User already exists")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    ResponseEntity<UserResponseDto> register(@RequestBody @Valid UserRequestDto body);
}
