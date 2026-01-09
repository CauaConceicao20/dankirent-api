package com.dankirent.api.controller;

import com.dankirent.api.model.user.User;
import com.dankirent.api.model.user.dto.UserRequestDto;
import com.dankirent.api.model.user.dto.UserResponseDto;
import com.dankirent.api.model.user.dto.UserUpdateDto;
import com.dankirent.api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping("/v1/create")
    public ResponseEntity<UserResponseDto> create(@RequestBody @Valid UserRequestDto body, UriComponentsBuilder uriBuilder) {
        User user = service.create(new User(body));
        URI uri = uriBuilder.path("/user/{id}").buildAndExpand(user.getId()).toUri();
        UriComponentsBuilder dummyUriBuilder = UriComponentsBuilder.newInstance();
        return ResponseEntity.created(uri).body(new UserResponseDto(user));
    }

    @GetMapping("/v1/getAll")
    public ResponseEntity<List<UserResponseDto>> getAll() {
        List<User> users = service.getAll();
        List<UserResponseDto> userDtos = users.stream().map(UserResponseDto::new).toList();
        return ResponseEntity.ok(userDtos);
    }

    @PutMapping("/v1/update/{id}")
    public ResponseEntity<UserResponseDto> update(@PathVariable("id") String id, @RequestBody @Valid UserUpdateDto body) {
        User updatedUser = service.update(UUID.fromString(id), new User(body));
        return ResponseEntity.ok(new UserResponseDto(updatedUser));
    }

    @DeleteMapping("/v1/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        service.delete(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }
}
