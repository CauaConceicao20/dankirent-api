package com.dankirent.api.service;

import com.dankirent.api.exception.personalized.StorageException;
import com.dankirent.api.infrastructure.storage.FileMetaData;
import com.dankirent.api.service.interfaces.FileStorage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StorageService {

    private final FileStorage fileStorage;

    private static final Logger log = LoggerFactory.getLogger(StorageService.class);

    public String uploadImage(MultipartFile file) {
        log.debug("Iniciando upload de arquivo: {}", file.getOriginalFilename());
        String newFileName = null;
        if (!file.isEmpty()) {
            newFileName = encodingFileName(file);
            fileStorage.uploadFile(file, newFileName);
        } else {
            log.error("Upload ignorado: arquivo vazio ({})", newFileName);
            throw new StorageException("Arquivo vazio");
        }
        return newFileName;
    }

    public FileMetaData getMetaData(String fileName) {
        return fileStorage.getMetaData(fileName);
    }

    public void deleteFile(String fileName) {
        log.debug("Iniciando exclusão do arquivo: {}", fileName);
        fileStorage.deleteFile(fileName);
    }

    private String encodingFileName(MultipartFile file){
        String originalName = Objects.requireNonNull(file.getOriginalFilename());
        return (UUID.randomUUID() + originalName.substring(originalName.lastIndexOf(".")))
                .replaceAll("[\n\r]", "_");
    }
}
