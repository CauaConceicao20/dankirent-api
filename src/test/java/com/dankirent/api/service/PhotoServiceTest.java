package com.dankirent.api.service;

import com.dankirent.api.model.photo.Photo;
import com.dankirent.api.model.user.User;
import com.dankirent.api.repository.PhotoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PhotoServiceTest {

    @Mock
    private PhotoRepository repository;

    @InjectMocks
    private PhotoService service;

    User user;
    Photo photo;
    Photo photoDataUpdate;

    @BeforeEach
    void setUp() {
        user = new User(UUID.randomUUID(), "FirstNameDefault", "LastNameDefault", "000.174.205-12", "71922224444",
                "teste122@gmail.com", "password123", photo, new HashSet<>());
        photo = new Photo(UUID.randomUUID(), user, "photo.png", "image/png", 1L, LocalDateTime.now());

        photoDataUpdate = new Photo(null, null, "updated_photo.png", "image/png", 2L, null);
    }

    @Test
    void shouldSavePhotoSuccessfully() {
        when(repository.save(any(Photo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Photo result = service.create(photo);

        verify(repository).save(any(Photo.class));

        assertEquals("photo.png", result.getFileName());
    }

    @Test
    void shouldGetAllPhotosSuccessfully() {
        when(repository.findAll()).thenReturn(List.of(photo));

        var result = service.getAll();

        verify(repository).findAll();

        assertEquals(1, result.size());
        assertEquals("photo.png", result.get(0).getFileName());
    }

    @Test
    void shouldGetPhotoByIdSuccessfully() {

        when(repository.findById(photo.getId())).thenReturn(Optional.of(photo));

        Photo result = service.getById(photo.getId());

        verify(repository).findById(photo.getId());

        assertEquals(photo.getId(), result.getId());
    }

    @Test
    void shouldThrowExceptionWhenPhotoNotFound() {
        when(repository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                service.getById(UUID.randomUUID()));

        verify(repository).findById(any(UUID.class));
    }

    @Test
    void shouldUpdatePhoto_whenDataAreValid() {
        when(repository.findById(photo.getId())).thenReturn((Optional.of(photo)));
        when(repository.save(any(Photo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Photo result = service.update(photo.getId(), photoDataUpdate);

        assertEquals("updated_photo.png", result.getFileName());
        assertEquals("image/png", result.getContentType());
        assertEquals(2L, result.getSize());

        verify(repository).findById(photo.getId());
        verify(repository).save(photo);
    }

    @Test
    void shouldUpdateOnlyNonNullFields() {
        when(repository.findById(photo.getId())).thenReturn((Optional.of(photo)));
        when(repository.save(any(Photo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Photo result = service.update(photo.getId(), new Photo());

        assertEquals("photo.png", result.getFileName());
        assertEquals("image/png", result.getContentType());
        assertEquals(1L, result.getSize());

        verify(repository).findById(photo.getId());
        verify(repository).save(photo);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenUserNotFoundOnUpdate() {
        when(repository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                service.update(UUID.randomUUID(), photoDataUpdate));

        verify(repository).findById(any(UUID.class));
        verify(repository, never()).save(any(Photo.class));
    }

    @Test
    void shouldDeletePhotoSuccessfully() {
        when(repository.findById(photo.getId())).thenReturn(Optional.of(photo));
        doNothing().when(repository).delete(photo);

        service.delete(photo.getId());

        verify(repository).findById(photo.getId());
        verify(repository).delete(photo);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentPhoto() {
        when(repository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                service.delete(UUID.randomUUID()));

        verify(repository).findById(any(UUID.class));
        verify(repository, never()).delete(any(Photo.class));
    }
}
