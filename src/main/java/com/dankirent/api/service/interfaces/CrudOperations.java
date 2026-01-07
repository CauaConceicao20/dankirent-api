package com.dankirent.api.service.interfaces;

import java.util.List;

public interface CrudOperations<T> {

    T create(T dto);

    List<T> getAll();

    T getById(Long id);

    T update(Long id, T dto);

    void delete(Long id);
}
