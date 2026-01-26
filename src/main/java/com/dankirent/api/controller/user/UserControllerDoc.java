package com.dankirent.api.controller.user;

import com.dankirent.api.config.SecurityConfig;
import com.dankirent.api.model.user.dto.UserRequestDto;
import com.dankirent.api.model.user.dto.UserResponseDto;
import com.dankirent.api.model.user.dto.UserUpdateDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Tag(name = "User")
@SecurityRequirement(name = SecurityConfig.SECURITY)
public interface UserControllerDoc {

    @Operation(summary = "Create a new user")
    @ApiResponse(responseCode = "201", description = "User created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid request body")
    @ApiResponse(responseCode = "409", description = "User already exists")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    ResponseEntity<UserResponseDto> create(@RequestBody @Valid UserRequestDto body, UriComponentsBuilder uriBuilder);

    @Operation(summary = "Get all users")
    @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    ResponseEntity<List<UserResponseDto>> getAll();

    @Operation(summary = "Update an existing user")
    @ApiResponse(responseCode = "200", description = "User updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid request body")
    @ApiResponse(responseCode = "404", description = "User not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    ResponseEntity<UserResponseDto> update(@PathVariable("id") String id, @RequestBody @Valid UserUpdateDto body);

    @Operation(summary = "Delete a user")
    @ApiResponse(responseCode = "204", description = "User deleted successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    ResponseEntity<Void> delete(@PathVariable("id") String id);

    @Operation(summary = "Update user's profile photo")
    @ApiResponse(responseCode = "200", description = "Profile photo updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid file upload")
    @ApiResponse(responseCode = "404", description = "User not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PostMapping(value = "/v1/updateProfilePhoto/{userId}/profile-photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<Void> updateProfilePhoto(@PathVariable("userId") String userId, @RequestParam("file") MultipartFile file);
}
