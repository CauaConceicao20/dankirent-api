package com.dankirent.api.infrastructure.storage;

import com.dankirent.api.exception.personalized.StorageException;
import com.dankirent.api.service.interfaces.FileStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class LocalFileStorage implements FileStorage {

    private static final Logger log = LoggerFactory.getLogger(LocalFileStorage.class);

    @Value("${upload.path}")
    private Path uploadDir;

    @Override
    public void uploadFile(MultipartFile file, String fileName) {
        try {
            log.info("Salvando arquivo: {}", file.getOriginalFilename());
            Files.createDirectories(uploadDir);
            Path destination = uploadDir.resolve(fileName);
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
            log.info("Arquivo salvo com sucesso: {}", fileName);
        } catch (IOException e) {
            log.error("Erro ao salvar arquivo:", e);
            throw new StorageException("Falha ao salvar arquivo");
        }
    }

    @Override
    public FileMetaData getMetaData(String fileName) {
        Path path = uploadDir.resolve(fileName).normalize();
        BasicFileAttributes attrs = null;
        try {
            log.debug("Obtendo metadados do arquivo: {}", fileName);
            attrs = Files.readAttributes(path, BasicFileAttributes.class);
            return new FileMetaData(fileName, attrs.size(), Files.probeContentType(path),
                    LocalDateTime.ofInstant(attrs.creationTime().toInstant(), ZoneId.systemDefault())
            );
        } catch (IOException e) {
            throw new StorageException("Falha ao obter metadados do arquivo");
        }
    }

    @Override
    public void deleteFile(String fileName) {
        Path path = uploadDir.resolve(fileName).normalize();
        try {
            Files.deleteIfExists(path);
            log.info("Arquivo excluído com sucesso: {}", fileName);
        } catch (IOException e) {
            log.error("Erro ao excluir arquivo:", e);
            throw new StorageException("Falha ao excluir arquivo");
        }

    }
}
