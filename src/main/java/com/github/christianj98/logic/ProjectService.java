package com.github.christianj98.logic;

import com.github.christianj98.TaskConfigurationProperties;
import com.github.christianj98.model.Project;
import com.github.christianj98.model.ProjectRepository;
import com.github.christianj98.model.TaskGroupRepository;
import com.github.christianj98.model.projection.GroupReadModel;
import com.github.christianj98.model.projection.GroupTaskWriteModel;
import com.github.christianj98.model.projection.GroupWriteModel;
import com.github.christianj98.model.projection.ProjectWriteModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

// @Service -> Defined in LogicConfiguration class
public class ProjectService {
    private final ProjectRepository projectRepository;

    private final TaskGroupRepository taskGroupRepository;

    private final TaskGroupService taskGroupService;

    private final TaskConfigurationProperties config;

    public ProjectService(final ProjectRepository projectRepository,
                          final TaskGroupRepository taskGroupRepository,
                          final TaskGroupService taskGroupService,
                          final TaskConfigurationProperties taskConfigurationProperties) {
        this.projectRepository = projectRepository;
        this.taskGroupRepository = taskGroupRepository;
        this.taskGroupService = taskGroupService;
        this.config = taskConfigurationProperties;
    }

    public List<Project> readAll() {
        return projectRepository.findAll();
    }

    public Project createProject(final ProjectWriteModel toSave) {
        return projectRepository.save(toSave.toProject());
    }

    // create group based on service
    public GroupReadModel createGroup(int projectId, LocalDateTime deadline) {
        if (!config.getTemplate().isAllowMultipleTasks() && taskGroupRepository.existsByDoneIsFalseAndProject_Id(projectId)) {
            throw new IllegalStateException("ony undone group from project is allowed");
        }
        return projectRepository.findById(projectId)
                .map(project -> {
                    var targetGroup = new GroupWriteModel();
                    targetGroup.setDescription(project.getDescription());
                    targetGroup.setTasks(
                            project.getSteps().stream()
                                    .map(projectStep -> {
                                        var task = new GroupTaskWriteModel();
                                        task.setDescription(projectStep.getDescription());
                                        task.setDeadline(deadline.plusDays(projectStep.getDaysToDeadline()));
                                        return task;
                                    }).collect(Collectors.toList()));
                    return taskGroupService.createGroup(targetGroup, project);
                }).orElseThrow(() -> new IllegalArgumentException("Project with given id not found"));
    }
}
