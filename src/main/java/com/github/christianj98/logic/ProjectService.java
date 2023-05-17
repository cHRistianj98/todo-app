package com.github.christianj98.logic;

import com.github.christianj98.TaskConfigurationProperties;
import com.github.christianj98.model.Project;
import com.github.christianj98.model.ProjectRepository;
import com.github.christianj98.model.Task;
import com.github.christianj98.model.TaskGroup;
import com.github.christianj98.model.TaskGroupRepository;
import com.github.christianj98.model.projection.GroupReadModel;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    private final TaskGroupRepository taskGroupRepository;

    private final TaskConfigurationProperties taskConfigurationProperties;

    public ProjectService(final ProjectRepository projectRepository,
                          final TaskGroupRepository taskGroupRepository,
                          final TaskConfigurationProperties taskConfigurationProperties) {
        this.projectRepository = projectRepository;
        this.taskGroupRepository = taskGroupRepository;
        this.taskConfigurationProperties = taskConfigurationProperties;
    }

    public List<Project> readAll() {
        return projectRepository.findAll();
    }

    public Project createProject(final Project project) {
        return projectRepository.save(project);
    }

    // create group based on service
    public GroupReadModel createGroup(int projectId, LocalDateTime deadline) {
        if (taskGroupRepository.existsByDoneIsFalseAndProject_Id(projectId)
                && !taskConfigurationProperties.getTemplate().isAllowMultipleTasks()) {
            throw new IllegalStateException("Only one group is allowed in the scope of project!");
        }

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Entity not found!"));

        final Set<Task> tasks = project.getSteps()
                .stream()
                .map(projectStep -> new Task(projectStep.getDescription(),
                        deadline.plusDays(projectStep.getDaysToDeadline())))
                .collect(Collectors.toSet());

        TaskGroup taskGroup = new TaskGroup();
        taskGroup.setDescription(project.getDescription());
        taskGroup.setDone(false);
        taskGroup.setProject(project);
        taskGroup.setTasks(tasks);
        taskGroupRepository.save(taskGroup);

        return new GroupReadModel(taskGroup);
    }
}
