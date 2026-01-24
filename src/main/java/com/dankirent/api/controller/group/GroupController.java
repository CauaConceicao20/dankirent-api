package com.dankirent.api.controller.group;

import com.dankirent.api.model.group.Group;
import com.dankirent.api.model.group.dto.GroupRequestDto;
import com.dankirent.api.model.group.dto.GroupResponseDto;
import com.dankirent.api.service.GroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/group")
@RequiredArgsConstructor
public class GroupController implements GroupControllerDoc{

    private final GroupService service;

    @Override
    @PostMapping("v1/create")
    public ResponseEntity<GroupResponseDto> create(@RequestBody @Valid GroupRequestDto body, UriComponentsBuilder uriBuilder) {
        Group group = service.create(new Group(body));
        URI uri = uriBuilder.path("/group/{id}").buildAndExpand(group.getId()).toUri();
        UriComponentsBuilder dummyUriBuilder = UriComponentsBuilder.newInstance();
        return ResponseEntity.created(uri).body(new GroupResponseDto(group));
    }

    @Override
    @GetMapping("v1/getAll")
    public ResponseEntity<List<GroupResponseDto>> getAll() {
        List<Group> groups = service.getAll();
        List<GroupResponseDto> responseDtos = groups.stream().map(GroupResponseDto::new).toList();
        return ResponseEntity.ok(responseDtos);
    }

    @Override
    @PutMapping("v1/update/{id}")
    public ResponseEntity<GroupResponseDto> update(@PathVariable("id") String id, @RequestBody @Valid GroupRequestDto body) {
        Group group = service.update(UUID.fromString(id), new Group(body));
        return ResponseEntity.ok(new GroupResponseDto(group));
    }

    @Override
    @DeleteMapping("v1/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        service.delete(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }
}
