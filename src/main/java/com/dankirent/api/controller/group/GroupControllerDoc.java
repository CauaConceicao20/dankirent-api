package com.dankirent.api.controller.group;

import com.dankirent.api.model.group.dto.GroupRequestDto;
import com.dankirent.api.model.group.dto.GroupResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Tag(name = "Group")
public interface GroupControllerDoc {

    @Operation(description = "Create a new group")
    @ApiResponse(responseCode = "201", description = "Group created successfully")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    ResponseEntity<GroupResponseDto> create(@RequestBody @Valid GroupRequestDto body, UriComponentsBuilder uriBuilder);

    @Operation(description = "Get all groups")
    @ApiResponse(responseCode = "200", description = "Groups retrieved successfully")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    ResponseEntity<List<GroupResponseDto>> getAll();

    @Operation(description = "Update a group")
    @ApiResponse(responseCode = "200", description = "Group updated successfully")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    ResponseEntity<GroupResponseDto> update(@PathVariable("id") String id, @RequestBody @Valid GroupRequestDto body);

    @Operation(description = "Delete a group")
    @ApiResponse(responseCode = "204", description = "Group deleted successfully")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    ResponseEntity<Void> delete(@PathVariable("id") String id);
}
