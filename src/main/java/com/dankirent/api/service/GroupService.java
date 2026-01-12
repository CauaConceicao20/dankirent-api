package com.dankirent.api.service;

import com.dankirent.api.model.group.Group;
import com.dankirent.api.repository.GroupRepository;
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
public class GroupService implements CrudOperations<Group> {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final GroupRepository repository;

    @Override
    public Group create(Group group) {
        log.debug("Criando group com nome={}", group.getName());
        repository.save(group);
        log.debug("Group criado com sucesso: id={}", group.getId());
        return group;
    }

    @Override
    public List<Group> getAll() {
        log.debug("Buscando todos os groups");
        return repository.findAll();
    }

    @Override
    public Group getById(UUID id) {
        log.debug("Buscando group com id={}", id);
        return repository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("Group não encontrado com id: " + id));
    }

    public Group getByName(String name) {
        log.debug("Buscando group com nome={}", name);
        return repository.findByName(name).
                orElseThrow(() -> new EntityNotFoundException("Group não encontrado com nome: " + name));
    }

    @Override
    public Group update(UUID id, Group entity) {
        log.debug("Atualizando group com id={}", id);
        Group group = getById(id);

        if (entity.getName() != null) group.setName(entity.getName());
        if (entity.getDescription() != null) group.setDescription(entity.getDescription());

        log.debug("Group atualizado com sucesso: id={}", id);
        return repository.save(group);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Deletando group com id={}", id);
        Group group = getById(id);
        repository.delete(group);
        log.debug("Group deletado com sucesso: id={}", id);
    }
}
