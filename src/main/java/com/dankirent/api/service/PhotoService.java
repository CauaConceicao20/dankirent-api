package com.dankirent.api.service;

import com.dankirent.api.model.photo.Photo;
import com.dankirent.api.repository.PhotoRepository;
import com.dankirent.api.service.interfaces.CrudOperations;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PhotoService implements CrudOperations<Photo> {

    private static final Logger log = LoggerFactory.getLogger(StorageService.class);
    private final PhotoRepository repository;

    @Override
    public Photo create(Photo photo) {
        log.debug("Criando foto para usuário id={}, filename={}", photo.getUser().getId(), photo.getFileName());
        repository.save(photo);
        log.debug("Foto persistida com sucesso: id={}", photo.getId());
        return photo;
    }

    @Override
    public List<Photo> getAll() {
        log.debug("Buscando todas as fotos");
        return repository.findAll();
    }

    @Override
    public Photo getById(UUID id) {
        log.debug("Buscando foto por id: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Foto não encontrada: id=" + id));
    }

    @Override
    public Photo update(UUID id, Photo entity) {
        Photo photo = getById(id);

        log.debug("Atualizando foto: id={}", id);

        if(entity.getFileName() != null) photo.setFileName(entity.getFileName());
        if(entity.getContentType() != null) photo.setContentType(entity.getContentType());
        if(entity.getSize() != null) photo.setSize(entity.getSize());

        repository.save(photo);

        log.debug("Foto atualizada com sucesso: id={}", id);

        return photo;
    }

    @Override
    public void delete(UUID id) {
        log.debug("Deletando foto por id: {}", id);
        Photo photo = getById(id);
        repository.delete(photo);
        log.debug("Foto deletada com sucesso: id={}", id);
    }
}
