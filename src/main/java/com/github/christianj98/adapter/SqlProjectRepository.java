package com.github.christianj98.adapter;

import com.github.christianj98.model.Project;
import com.github.christianj98.model.ProjectRepository;
import com.github.christianj98.model.TaskGroup;
import com.github.christianj98.model.TaskGroupRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SqlProjectRepository extends ProjectRepository, JpaRepository<Project, Integer> {
    @Override
    // Get rid of the problem with n + 1 selects by fetching all tasks
    @Query("SELECT DISTINT p FROM Project p JOIN FETCH p.steps")
    List<Project> findAll();

}
