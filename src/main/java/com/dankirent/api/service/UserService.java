package com.dankirent.api.service;

import com.dankirent.api.exception.personalized.StorageException;
import com.dankirent.api.infrastructure.storage.FileMetaData;
import com.dankirent.api.model.photo.Photo;
import com.dankirent.api.model.user.User;
import com.dankirent.api.repository.UserRepository;
import com.dankirent.api.service.interfaces.CrudOperations;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements CrudOperations<User> {

    private final UserRepository repository;
    private final PhotoService photoService;
    private final StorageService storageService;

    @Override
    @Transactional
    public User create(User user) {
        final String DEFAULT_IMAGE_NAME = "default_user_photo.png";
        try {
            FileMetaData metaData = storageService.getMetaData(DEFAULT_IMAGE_NAME);
            repository.save(user);
            Photo photo = new Photo(null, user, metaData.getFileName(), "image/png", metaData.getSize(), metaData.getCreatedAt());
            System.out.println(photo.getContentType());
            photoService.create(photo);

            return user;
        }catch(IOException e) {
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
}
