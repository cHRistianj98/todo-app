package com.github.christianj98.adapter;

import com.github.christianj98.model.TaskGroup;
import com.github.christianj98.model.TaskGroupRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SqlTaskGroupRepository extends TaskGroupRepository, JpaRepository<TaskGroup, Integer> {
    @Override
    // Get rid of the problem with n + 1 selects by fetching all tasks
    @Query("FROM TaskGroup g JOIN FETCH g.tasks")
    List<TaskGroup> findAll();
}
