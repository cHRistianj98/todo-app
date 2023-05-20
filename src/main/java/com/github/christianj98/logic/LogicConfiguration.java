package com.github.christianj98.logic;

import com.github.christianj98.TaskConfigurationProperties;
import com.github.christianj98.adapter.SqlTaskRepository;
import com.github.christianj98.model.ProjectRepository;
import com.github.christianj98.model.TaskGroupRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Alternative way to configure Dependency Injection for Project Service
@Configuration
//@ImportResource("classpath:applicationContext.xml")
public class LogicConfiguration {

    @Bean
    ProjectService projectService(final ProjectRepository projectRepository,
                                  final TaskGroupRepository taskGroupRepository,
                                  final TaskConfigurationProperties taskConfigurationProperties) {
        return new ProjectService(projectRepository, taskGroupRepository, taskConfigurationProperties);
    }

    @Bean
    TaskGroupService taskGroupService(final TaskGroupRepository taskGroupRepository,
                                      final SqlTaskRepository taskRepository) {
        return new TaskGroupService(taskGroupRepository, taskRepository);
    }
}
