package com.dankirent.api.service;

import com.dankirent.api.model.photo.Photo;
import com.dankirent.api.repository.PhotoRepository;
import com.dankirent.api.service.interfaces.CrudOperations;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PhotoService implements CrudOperations<Photo> {

    private final PhotoRepository repository;

    @Override
    public Photo create(Photo photo) {
        return repository.save(photo);
    }

    @Override
    public List<Photo> getAll() {
        return List.of();
    }

    @Override
    public Photo getById(Long id) {
        return null;
    }

    @Override
    public Photo update(Long id, Photo dto) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
