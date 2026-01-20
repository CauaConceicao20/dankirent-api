package com.dankirent.api.service;

import com.dankirent.api.exception.personalized.StorageException;
import com.dankirent.api.infrastructure.storage.FileMetaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(StorageService.class);
    private final Path uploadDir;

    public StorageService(@Value("${upload.path}") String uploadPath) {
        this.uploadDir = Paths.get(uploadPath);
    }

    public void uploadImage(MultipartFile file) {
        log.debug("Iniciando upload de arquivo: {}", file.getOriginalFilename());
        if (!file.isEmpty()) {
            log.info("Salvando arquivo: {}", file.getOriginalFilename());
            try {
                Files.createDirectories(uploadDir);
                Path destination = uploadDir.resolve(Objects.requireNonNull(file.getOriginalFilename()));

                Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
                log.info("Arquivo salvo com sucesso: {}", file.getOriginalFilename());
            } catch (IOException exception) {
                log.error("Erro ao salvar arquivo:", exception);
                throw new StorageException("Falha ao salvar arquivo");
            }
        }else {
            log.error("Upload ignorado: arquivo vazio ({})", file.getOriginalFilename());
            throw new StorageException("Arquivo vazio");
        }
    }

    public FileMetaData getMetaData(String fileName) throws IOException {
        log.debug("Obtendo metadados do arquivo: {}", fileName);
        Path path = uploadDir.resolve(fileName).normalize();
        BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
        return new FileMetaData(fileName, attrs.size(), Files.probeContentType(uploadDir),
                LocalDateTime.ofInstant(attrs.creationTime().toInstant(), ZoneId.systemDefault())
        );
    }

    public void deleteImage(String fileName) {
        log.debug("Iniciando exclusão do arquivo: {}", fileName);
        Path path = uploadDir.resolve(fileName).normalize();
        try {
            Files.deleteIfExists(path);
            log.info("Arquivo excluído com sucesso: {}", fileName);
        } catch (IOException exception) {
            log.error("Erro ao excluir arquivo:", exception);
            throw new StorageException("Falha ao excluir arquivo");
        }
    }
}
