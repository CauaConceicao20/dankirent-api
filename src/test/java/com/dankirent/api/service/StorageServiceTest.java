package com.dankirent.api.service;

import com.dankirent.api.exception.personalized.StorageException;
import com.dankirent.api.infrastructure.storage.FileMetaData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
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

    private StorageService storageService;

    MultipartFile file;
    Path pathInvalid;

    @BeforeEach
    void setUp() {
        storageService = new StorageService("target/test-uploads");
        pathInvalid = mock(Path.class);
        file = new MockMultipartFile("file", "test.txt", "text/plain", "Hello World".getBytes());

    }

    @Test
    void shouldUploadFileSuccessfully_WhenValidFileProvided() throws IOException {
        String result = storageService.uploadImage(file);

        Path uploadedFile = Paths.get("target/test-uploads").resolve(result);
        assertTrue(Files.exists(uploadedFile));

        Files.deleteIfExists(uploadedFile);
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
    void shouldThrowStorageException_WhenFilesCreateDirectoriesFails() throws IOException {
        try(MockedStatic<Files> filesMockedStatic = mockStatic(Files.class)){
            filesMockedStatic.when(() -> Files.createDirectories(any(Path.class))).thenThrow(IOException.class);

            assertThrows(StorageException.class, () ->
                    storageService.uploadImage(file));
        }
    }

    @Test
    void shouldGetMetaDataSuccessfully_WhenValidFileNameProvided() throws IOException {
        String uploadedFileName = storageService.uploadImage(file);

        FileMetaData metaData = storageService.getMetaData(uploadedFileName);

        assertEquals(uploadedFileName, metaData.getFileName());
        assertEquals(file.getSize(), metaData.getSize());

        Files.deleteIfExists(Paths.get("target/test-uploads").resolve(uploadedFileName));
    }

    @Test
    void shouldDeleteFileSuccessfully_WhenValidFileNameProvided() throws IOException {
        String uploadedFileName = storageService.uploadImage(file);

        storageService.deleteFile(uploadedFileName);

        Path uploadedFile = Paths.get("target/test-uploads").resolve(uploadedFileName);
        assertFalse(Files.exists(uploadedFile));
    }

    @Test
    void shouldThrowStorageException_WhenDeletingNonExistentFile() {
        try(MockedStatic<Files> filesMockedStatic = mockStatic(Files.class)){
            filesMockedStatic.when(() -> Files.deleteIfExists(any(Path.class))).thenThrow(IOException.class);

            assertThrows(StorageException.class, () ->
                    storageService.deleteFile(anyString()));
        }
    }
}
