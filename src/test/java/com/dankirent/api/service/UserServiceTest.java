package com.dankirent.api.service;

import com.dankirent.api.exception.personalized.StorageException;
import com.dankirent.api.infrastructure.storage.FileMetaData;
import com.dankirent.api.model.group.Group;
import com.dankirent.api.model.photo.Photo;
import com.dankirent.api.model.user.User;
import com.dankirent.api.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

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
    User userDataUpdate;
    Photo existingPhoto;
    MultipartFile file;

    @BeforeEach
    void setUp() {
        file = mock(MultipartFile.class);

        existingPhoto = new Photo(UUID.randomUUID(), user, "old-photo.png", "", 1L, LocalDateTime.now());

        user = new User(UUID.randomUUID(), "FirstNameDefault", "LastNameDefault", "000.174.205-12", "71922224444",
                "teste122@gmail.com", "password123", existingPhoto, new HashSet<>());

        group = new Group(UUID.randomUUID(), "USER", "Default user group",
                new HashSet<>(), new HashSet<>());

        fileMetaData = new FileMetaData("default_user_photo.png", 766, "image/png",
                LocalDateTime.of(2023, 1, 1, 12, 0));

        userDataUpdate = new User(null, "UpdatedFirstName", "UpdatedLastName", null, "71922224444",
                null, null, null, new HashSet<>());
    }

    @Test
    void shouldCreatUser_whenDataAreValid() throws IOException {
        when(groupService.getByName("USER")).thenReturn(group);
        when(storageService.getMetaData("default_user_photo.png")).thenReturn(fileMetaData);
        when(repository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = service.create(user);

        assertEquals("FirstNameDefault", result.getFirstName());
        assertEquals(1, result.getUserGroups().size());

        verify(groupService).getByName("USER");
        verify(repository).save(any(User.class));
        verify(photoService).create(any(Photo.class));
    }

    @Test
    void shouldThrowEntityNotFoundException_whenGroupNotFoundOnCreateUser() {
        when(groupService.getByName(anyString())).thenThrow(new EntityNotFoundException());

        assertThrows(EntityNotFoundException.class, () -> {
            service.create(user);
        });

        verify(groupService).getByName(anyString());
        verify(repository, never()).save(any(User.class));
    }

    @Test
    void shouldThrowStorageException_whenGetMetaDataFailsOnCreateUser() throws IOException {
        when(groupService.getByName("USER")).thenReturn(group);
        when(repository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(storageService.getMetaData(anyString())).thenThrow(new IOException());

        assertThrows(StorageException.class, () -> {
            service.create(user);
        });

        verify(groupService).getByName("USER");
        verify(repository).save(any(User.class));
        verify(storageService).getMetaData(anyString());
    }

    @Test
    void shouldGetAllUsers() {
        when(repository.findAll()).thenReturn(Collections.singletonList(user));
        List<User> result = service.getAll();

        assertEquals(1, result.size());

        verify(repository).findAll();
    }

    @Test
    void shouldGetUserById_whenUserExists() {
        when(repository.findById(user.getId())).thenReturn(Optional.of(user));

        User result = service.getById(user.getId());

        assertEquals(user.getId(), result.getId());

        verify(repository).findById(user.getId());
    }

    @Test
    void shouldThrowEntityNotFoundException_whenUserNotFoundById() {
        when(repository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            service.getById(UUID.randomUUID());
        });

        verify(repository).findById(any(UUID.class));
    }

    @Test
    void shouldUpdateUser_whenDataAreValid() {
        when(repository.findById(user.getId())).thenReturn((Optional.of(user)));
        when(repository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = service.update(user.getId(), userDataUpdate);

        assertEquals("UpdatedFirstName", result.getFirstName());
        assertEquals("UpdatedLastName", result.getLastName());
        assertEquals("71922224444", result.getPhoneNumber());

        verify(repository).findById(user.getId());
        verify(repository).save(user);
    }

    @Test
    void shouldUpdateOnlyNonNullFields() {
        when(repository.findById(user.getId())).thenReturn((Optional.of(user)));
        when(repository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = service.update(user.getId(), new User());

        assertEquals("FirstNameDefault", result.getFirstName());
        assertEquals("LastNameDefault", result.getLastName());
        assertEquals("71922224444", result.getPhoneNumber());

        verify(repository).findById(user.getId());
        verify(repository).save(user);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenUserNotFoundOnUpdate() {
        when(repository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            service.update(UUID.randomUUID(), userDataUpdate);
        });

        verify(repository).findById(any(UUID.class));
        verify(repository, never()).save(any(User.class));
    }

    @Test
    void shouldDeleteUserById() {
        when(repository.findById(user.getId())).thenReturn(Optional.of(user));

        service.delete(user.getId());

        verify(repository).findById(user.getId());
        verify(repository).delete(user);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenUserNotFoundOnDelete() {
        when(repository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            service.delete(UUID.randomUUID());
        });

        verify(repository).findById(any(UUID.class));
        verify(repository, never()).delete(any(User.class));
    }

    @Test
    void shouldUpdateProfilePhoto_whenUserAndFileAreValid() {
        when(repository.findById(user.getId())).thenReturn(Optional.of(user));

        service.updateProfilePhoto(user.getId(), file);

        verify(repository).findById(user.getId());
        verify(storageService).uploadImage(file);
        verify(photoService).update(eq(existingPhoto.getId()), any(Photo.class));
        verify(storageService).deleteImage("old-photo.png");
    }

    @Test
    void shouldThrowEntityNotFoundException_whenUserNotFoundOnUpdateProfilePhoto() {
        when(repository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            service.updateProfilePhoto(UUID.randomUUID(), file);
        });

        verify(repository).findById(any(UUID.class));
        verify(storageService, never()).uploadImage(any());
        verify(photoService, never()).update(any(), any());
        verify(storageService, never()).deleteImage(anyString());
    }

    @Test
    void shouldThrowStorageException_whenUploadImageFailsOnUpdateProfilePhoto() {
        when(repository.findById(user.getId())).thenReturn(Optional.of(user));
        when(storageService.uploadImage(file)).thenThrow(new StorageException("Upload falhou"));

        assertThrows(StorageException.class, () -> {
            service.updateProfilePhoto(user.getId(), file);
        });

        verify(repository).findById(user.getId());
        verify(storageService).uploadImage(file);
        verify(photoService, never()).update(any(), any());
        verify(storageService, never()).deleteImage(anyString());
    }
}

