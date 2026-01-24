package com.dankirent.api.controller.user;

import com.dankirent.api.model.user.User;
import com.dankirent.api.model.user.dto.UserRequestDto;
import com.dankirent.api.model.user.dto.UserResponseDto;
import com.dankirent.api.model.user.dto.UserUpdateDto;
import com.dankirent.api.service.StorageService;
import com.dankirent.api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserController implements UserControllerDoc {

    private final UserService service;

    @Override
    @PostMapping("/v1/create")
    public ResponseEntity<UserResponseDto> create(@RequestBody @Valid UserRequestDto body, UriComponentsBuilder uriBuilder) {
        User user = service.create(new User(body));
        URI uri = uriBuilder.path("/user/{id}").buildAndExpand(user.getId()).toUri();
        UriComponentsBuilder dummyUriBuilder = UriComponentsBuilder.newInstance();
        return ResponseEntity.created(uri).body(new UserResponseDto(user));
    }

    @Override
    @GetMapping("/v1/getAll")
    public ResponseEntity<List<UserResponseDto>> getAll() {
        List<User> users = service.getAll();
        List<UserResponseDto> userDtos = users.stream().map(UserResponseDto::new).toList();
        return ResponseEntity.ok(userDtos);
    }

    @Override
    @PutMapping("/v1/update/{id}")
    public ResponseEntity<UserResponseDto> update(@PathVariable("id") String id, @RequestBody @Valid UserUpdateDto body) {
        User updatedUser = service.update(UUID.fromString(id), new User(body));
        return ResponseEntity.ok(new UserResponseDto(updatedUser));
    }

    @Override
    @DeleteMapping("/v1/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        service.delete(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }

    @Override
    @PostMapping(value = "/v1/updateProfilePhoto/{userId}/profile-photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateProfilePhoto(@PathVariable("userId") String userId, @RequestParam("file") MultipartFile file) {
        service.updateProfilePhoto(UUID.fromString(userId), file);
        return ResponseEntity.ok().build();
    }
}
