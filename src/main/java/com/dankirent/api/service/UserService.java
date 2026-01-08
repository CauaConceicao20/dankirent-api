package com.dankirent.api.service;

import com.dankirent.api.exception.personalized.StorageException;
import com.dankirent.api.infrastructure.storage.FileMetaData;
import com.dankirent.api.model.group.Group;
import com.dankirent.api.model.photo.Photo;
import com.dankirent.api.model.user.User;
import com.dankirent.api.model.user.UserGroup;
import com.dankirent.api.repository.UserRepository;
import com.dankirent.api.service.interfaces.CrudOperations;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements CrudOperations<User> {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository repository;
    private final PhotoService photoService;
    private final StorageService storageService;
    private final GroupService groupService;

    @Override
    @Transactional
    public User create(User user) {
        log.debug("Criando novo usuário");
        try {
            assignDefaultGroup(user);
            repository.save(user);
            assignDefaultPhoto(user);
            log.info("Usuário criado com sucesso: id={}", user.getId());
            return user;
        } catch (IOException e) {
            log.error("Erro ao atribuir foto padrão ao usuário", e);
            throw new StorageException("Falha ao ler metadados do arquivo");
        }
    }

    @Override
    public List<User> getAll() {
        return List.of();
    }

    @Override
    public User getById(Long id) {
        return null;
    }

    @Override
    public User update(Long id, User dto) {
        return null;
    }

    @Override
    public void delete(Long id) {
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
