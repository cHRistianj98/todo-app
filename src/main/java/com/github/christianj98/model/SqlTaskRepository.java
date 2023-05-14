package com.github.christianj98.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SqlTaskRepository extends TaskRepository, JpaRepository<Task, Integer> {

    @Override
    @Query(nativeQuery = true, value = "SELECT COUNT(*) > 0 FROM TASKS WHERE id=:id")
    // @Query(nativeQuery = true, value = "SELECT COUNT(*) > 0 FROM TASKS WHERE id=?1")
    boolean existsById(Integer id);
}
