package com.dankirent.api.service;

import com.dankirent.api.model.group.Group;
import com.dankirent.api.repository.GroupRepository;
import com.dankirent.api.service.interfaces.CrudOperations;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService implements CrudOperations<Group> {

    private final GroupRepository repository;

    @Override
    public Group create(Group group) {
        return repository.save(group);
    }

    @Override
    public List<Group> getAll() {
        return List.of();
    }

    @Override
    public Group getById(Long id) {
        return null;
    }

    public Group getByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public Group update(Long id, Group dto) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
