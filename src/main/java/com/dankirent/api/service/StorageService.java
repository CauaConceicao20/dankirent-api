package com.dankirent.api.service;

import com.dankirent.api.exception.personalized.StorageException;
import com.dankirent.api.infrastructure.storage.FileMetaData;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

@Service
public class StorageService {

    private final Path uploadDir;

    public StorageService(@Value("${upload.path}") String uploadPath) {
        this.uploadDir = Paths.get(uploadPath);
    }

    public Boolean uploadImage(MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                Files.createDirectories(uploadDir);
                Path destination = uploadDir.resolve(Objects.requireNonNull(file.getOriginalFilename()));

                Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

            } catch (IOException exception) {
                throw new StorageException("Falha ao salvar arquivo");
            }
        }
        return true;
    }

    public FileMetaData getMetaData(String fileName) throws IOException {
        Path path = uploadDir.resolve(fileName).normalize();
        BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
        return new FileMetaData(fileName, attrs.size(), Files.probeContentType(uploadDir),
                LocalDateTime.ofInstant(attrs.creationTime().toInstant(), ZoneId.systemDefault())
        );
    }
}
