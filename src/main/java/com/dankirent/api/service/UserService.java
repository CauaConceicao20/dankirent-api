package com.dankirent.api.service;

import com.dankirent.api.exception.personalized.StorageException;
import com.dankirent.api.infrastructure.storage.FileMetaData;
import com.dankirent.api.model.group.Group;
import com.dankirent.api.model.photo.Photo;
import com.dankirent.api.model.photo.dto.PhotoUpdateDto;
import com.dankirent.api.model.user.User;
import com.dankirent.api.model.user.UserGroup;
import com.dankirent.api.repository.UserRepository;
import com.dankirent.api.service.interfaces.CrudOperations;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements CrudOperations<User> {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository repository;
    private final PhotoService photoService;
    private final StorageService storageService;
    private final GroupService groupService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User create(User user) {
        log.debug("Criando novo usuário");
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            assignDefaultGroup(user);
            User userSaved = repository.save(user);
            assignDefaultPhoto(userSaved);
            log.info("Usuário criado com sucesso: id={}", user.getId());
            return userSaved;
        } catch (IOException e) {
            log.error("Erro ao atribuir foto padrão ao usuário", e);
            throw new StorageException("Falha ao ler metadados do arquivo");
        }
    }

    @Override
    public List<User> getAll() {
        log.debug("Buscando todos os usuários");
        return repository.findAll();
    }

    @Override
    public User getById(UUID id) {
        log.debug("Buscando usuário por id: {}", id);
        return repository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado: id=" + id));
    }

    @Override
    public User update(UUID id, User userData) {
        log.debug("Atualizando usuário: id={}", id);
        User user = getById(id);

        if (userData.getFirstName() != null)
            user.setFirstName(userData.getFirstName());
        if (userData.getLastName() != null)
            user.setLastName(userData.getLastName());
        if (userData.getPhoneNumber() != null)
            user.setPhoneNumber(userData.getPhoneNumber());

        log.debug("Dados do usuário atualizados: id={}", id);

        return repository.save(user);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Deletando usuário: id={}", id);
        User user = getById(id);
        repository.delete(user);
        log.debug("Usuário deletado com sucesso: id={}", id);
    }

    @Transactional
    public void updateProfilePhoto(UUID userId, MultipartFile file) {
        log.debug("Atualizando foto de perfil do usuário: id={}", userId);
        User user = getById(userId);
        String oldFileName = user.getPhoto().getFileName();
        String newFilename = storageService.uploadImage(file);
        Photo photo = new Photo(new PhotoUpdateDto(file));
        photo.setFileName(newFilename);
        photoService.update(user.getPhoto().getId(), photo);
        storageService.deleteFile(oldFileName);
        log.debug("Foto de perfil atualizada com sucesso para o usuário: id={}", userId);
    }

    private void assignDefaultGroup(User user) {
        log.debug("Atribuindo grupo padrão ao usuário: {}", user.getFirstName());
        Group group = groupService.getByName("USER");
        UserGroup userGroup = new UserGroup(group, user);
        user.getUserGroups().add(userGroup);
    }

    private void assignDefaultPhoto(User user) throws IOException {
        log.debug("Atribuindo foto padrão ao usuário: {}", user.getFirstName());
        final String DEFAULT_IMAGE_NAME = "default_user_photo.png";
        final String DEFAULT_CONTENT_TYPE = "image/png";
        FileMetaData metaData = storageService.getMetaData(DEFAULT_IMAGE_NAME);
        Photo photo = new Photo(null, user, metaData.getFileName(), DEFAULT_CONTENT_TYPE, metaData.getSize(), metaData.getCreatedAt());
        photoService.create(photo);
    }
}
