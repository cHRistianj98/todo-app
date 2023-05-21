package com.github.christianj98.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TaskRepositoryTest implements TaskRepository {

    private Map<Integer, Task> tasks = new HashMap<>();
    @Override
    public List<Task> findAll() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public Page<Task> findAll(final Pageable page) {
        return null;
    }

    @Override
    public Optional<Task> findById(final Integer id) {
        return Optional.ofNullable(tasks.get(id));
    }

    @Override
    public Task save(final Task entity) {
        final int key = tasks.size() + 1;
        entity.setId(key);
        tasks.put(key, entity);
        return tasks.get(key);
    }

    @Override
    public boolean existsById(final Integer id) {
        return tasks.containsKey(id);
    }

    @Override
    public boolean existsByDoneIsFalseAndGroup_Id(final Integer groupId) {
        return false;
    }

    @Override
    public List<Task> findByDone(final boolean done) {
        return null;
    }

    @Override
    public List<Task> findByGroup_Id(final int groupId) {
        return null;
    }
}
