package com.dankirent.api.service.interfaces;

import java.util.List;
import java.util.UUID;

public interface CrudOperations<T> {

    T create(T entity);

    List<T> getAll();

    T getById(UUID id);

    T update(UUID id, T entity);

    void delete(UUID id);
}
