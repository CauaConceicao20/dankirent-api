package com.dankirent.api.service;

import com.dankirent.api.exception.personalized.StorageException;
import com.dankirent.api.infrastructure.storage.FileMetaData;
import com.dankirent.api.service.interfaces.FileStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StorageServiceTest {

    @Mock
    private FileStorage fileStorage;

    @InjectMocks
    private StorageService storageService;

    MultipartFile file;

    @BeforeEach
    void setUp() {
        file = new MockMultipartFile("file", "test.txt", "text/plain", "Hello World".getBytes());
    }

    @Test
    void shouldUploadFileSuccessfully_WhenValidFileProvided() throws IOException {
        String uploadedFileName = storageService.uploadImage(file);

        assertNotEquals(file.getName(), uploadedFileName);

        verify(fileStorage, times(1)).uploadFile(any(MultipartFile.class), anyString());

        Files.deleteIfExists(Paths.get("target/test-uploads").resolve(uploadedFileName));
    }

    @Test
    void shouldGenerateUniqueFileNames_ForMultipleUploads() throws IOException {
        String firstUpload = storageService.uploadImage(file);
        String secondUpload = storageService.uploadImage(file);

        assertNotEquals(firstUpload, secondUpload);

        Files.deleteIfExists(Paths.get("target/test-uploads").resolve(firstUpload));
        Files.deleteIfExists(Paths.get("target/test-uploads").resolve(secondUpload));
    }

    @Test
    void shouldThrowStorageException_WhenUploadingEmptyFile() {
        MultipartFile emptyFile = new MockMultipartFile("file", "", "text/plain", new byte[0]);

        assertThrows(StorageException.class, () ->
                storageService.uploadImage(emptyFile));
    }

    @Test
    void shouldGetMetaDataSuccessfully_WhenValidFileNameProvided() throws IOException {
        String uploadedFileName = storageService.uploadImage(file);
        when(fileStorage.getMetaData(anyString())).thenReturn(
                new FileMetaData(uploadedFileName, file.getSize(), "text/plain", null));

        FileMetaData metaData = storageService.getMetaData(uploadedFileName);

        assertEquals(uploadedFileName, metaData.getFileName());
        assertEquals(file.getSize(), metaData.getSize());

        verify(fileStorage).uploadFile(file, uploadedFileName);

        Files.deleteIfExists(Paths.get("target/test-uploads").resolve(uploadedFileName));
    }

    @Test
    void shouldDeleteFileSuccessfully_WhenValidFileNameProvided() throws IOException {
        String uploadedFileName = storageService.uploadImage(file);

        storageService.deleteFile(uploadedFileName);

        Path uploadedFile = Paths.get("target/test-uploads").resolve(uploadedFileName);

        verify(fileStorage).deleteFile(uploadedFileName);

        assertFalse(Files.exists(uploadedFile));
    }
}
