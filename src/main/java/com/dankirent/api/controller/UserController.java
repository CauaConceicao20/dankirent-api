package com.dankirent.api.controller;

import com.dankirent.api.model.user.User;
import com.dankirent.api.model.user.dto.UserRequestDto;
import com.dankirent.api.model.user.dto.UserResponseDto;
import com.dankirent.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping("/v1/create")
    public ResponseEntity<UserResponseDto> create(@RequestBody UserRequestDto body,  UriComponentsBuilder uriBuilder) {
        User user = service.create(new User(body));
        URI uri = uriBuilder.path("/user/{id}").buildAndExpand(user.getId()).toUri();
        UriComponentsBuilder dummyUriBuilder = UriComponentsBuilder.newInstance();
        return ResponseEntity.created(uri).body(new UserResponseDto(user));
    }
}
