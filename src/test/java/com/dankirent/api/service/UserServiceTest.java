package com.dankirent.api.service;

import com.dankirent.api.infrastructure.storage.FileMetaData;
import com.dankirent.api.model.group.Group;
import com.dankirent.api.model.photo.Photo;
import com.dankirent.api.model.user.User;
import com.dankirent.api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private PhotoService photoService;

    @Mock
    private GroupService groupService;

    @Mock
    private StorageService storageService;

    @InjectMocks
    private UserService service;

    User user;
    Group group;
    FileMetaData fileMetaData;

    @BeforeEach
    void setUp() {
        user = new User(null, "FirstName", "LastName", "000.174.205-12", "71922224444",
                "teste122@gmail.com", "password123", new HashSet<>());

        group = new Group(UUID.randomUUID(), "USER", "Default user group",
                new HashSet<>(), new HashSet<>());

        fileMetaData = new FileMetaData("default_user_photo.png", 766, "image/png",
                LocalDateTime.of(2023, 1, 1, 12, 0));
    }

    @Test
     void shouldCreatUser_whenDataAreValid() throws IOException {
        when(groupService.getByName("USER")).thenReturn(group);
        when(storageService.getMetaData("default_user_photo.png")).thenReturn(fileMetaData);
        when(repository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = service.create(user);

        assertEquals("FirstName", result.getFirstName());
        assertEquals(1, result.getUserGroups().size());

        verify(groupService).getByName("USER");
        verify(repository).save(user);
        verify(photoService).create(any(Photo.class));
    }

    @Test
    void shouldGetAllUsers() {
        List<User> users = Collections.singletonList(user);

        when(repository.findAll()).thenReturn(users);
        List<User> result = service.getAll();

        assertEquals(1, result.size());

        verify(repository).findAll();
    }
}

