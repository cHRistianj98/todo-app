package com.github.christianj98.adapter;

import com.github.christianj98.model.Task;
import com.github.christianj98.model.TaskRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SqlTaskRepository extends TaskRepository, JpaRepository<Task, Integer> {

    @Override
    @Query(nativeQuery = true, value = "SELECT COUNT(*) > 0 FROM TASKS WHERE id=:id")
        // @Query(nativeQuery = true, value = "SELECT COUNT(*) > 0 FROM TASKS WHERE id=?1")
    boolean existsById(Integer id);

    @Override
    boolean existsByDoneIsFalseAndGroup_Id(Integer groupId);
}
