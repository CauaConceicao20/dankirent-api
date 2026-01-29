package com.dankirent.api.service;

import com.dankirent.api.model.group.Group;
import com.dankirent.api.model.permission.Permission;
import com.dankirent.api.model.user.UserGroup;
import com.dankirent.api.repository.GroupRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GroupServiceTest {

    @Mock
    private GroupRepository repository;

    @InjectMocks
    private GroupService service;

    Group group;

    Group updatedGroup;

    @BeforeEach
    void setUp() {
        group = new Group(null, "Admins", "Administrators Group",
                new HashSet<UserGroup>(), new HashSet<Permission>());

        updatedGroup = new Group(null, "Updated Admins", "Updated Administrators Group",
                new HashSet<UserGroup>(), new HashSet<Permission>());
    }

    @Test
    void shouldCreateGroup_WhenValidDataProvided() {
        when(repository.save(any(Group.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Group result = service.create(group);

        assertEquals("Admins", result.getName());

        verify(repository).save(any(Group.class));
    }

    @Test
    void shouldGetGroupById_WhenGroupExists() {
        when(repository.findById(any()))
                .thenReturn(java.util.Optional.of(group));

        Group result = service.getById(group.getId());

        assertEquals("Admins", result.getName());

        verify(repository).findById(any());
    }

    @Test
    void shouldThrowException_WhenGroupNotFoundById() {
        when(repository.findById(any()))
                .thenReturn(java.util.Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                service.getById(group.getId()));

        verify(repository).findById(any());
    }

    @Test
    void shouldGetAllGroupsSuccessfully() {
        when(repository.findAll()).thenReturn(java.util.List.of(group));

        List<Group> result = service.getAll();

        assertEquals(1, result.size());
        assertEquals("Admins", result.get(0).getName());

        verify(repository).findAll();
    }

    @Test
    void shouldGetByName_WhenGroupExists() {
        when(repository.findByName(anyString()))
                .thenReturn(Optional.of(group));

        Group result = service.getByName("Admins");

        assertEquals("Admins", result.getName());

        verify(repository).findByName(anyString());
    }

    @Test
    void shouldThrowException_WhenGroupNotFoundByName() {
        when(repository.findByName(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                service.getByName("NonExistentGroup"));

        verify(repository).findByName(anyString());
    }

    @Test
    void shouldUpdateGroup_WhenGroupExists() {
        when(repository.findById(any())).thenReturn(Optional.of(group));
        when(repository.save(any(Group.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Group result = service.update(group.getId(), updatedGroup);

        assertEquals("Updated Admins", result.getName());
        assertEquals("Updated Administrators Group", result.getDescription());

        verify(repository).findById(any());
        verify(repository).save(any(Group.class));
    }

    @Test
    void shouldThrowException_WhenUpdatingNonExistentGroup() {
        when(repository.findById(any())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                service.update(group.getId(), updatedGroup));

        verify(repository).findById(any());
    }

    @Test
    void shouldUpdateOnlyNonNullFields_WhenUpdatingGroup() {
        when(repository.findById(any())).thenReturn(Optional.of(group));
        when(repository.save(any(Group.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Group result = service.update(group.getId(), new Group());

        assertEquals("Admins", result.getName());
        assertEquals("Administrators Group", result.getDescription());

        verify(repository).findById(any());
        verify(repository).save(any(Group.class));
    }

    @Test
    void shouldDeleteGroup_WhenGroupExists() {
        when(repository.findById(any())).thenReturn(Optional.of(group));

        service.delete(group.getId());

        verify(repository).findById(any());
        verify(repository).delete(any(Group.class));
    }
}
