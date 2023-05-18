package com.github.christianj98.logic;

import com.github.christianj98.TaskConfigurationProperties;
import com.github.christianj98.model.ProjectRepository;
import com.github.christianj98.model.TaskGroupRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

// Alternative way to configure Dependency Injection for Project Service
@Configuration
@ImportResource("classpath:applicationContext.xml")
public class LogicConfiguration {

    @Bean
    ProjectService service(final ProjectRepository projectRepository,
                           final TaskGroupRepository taskGroupRepository,
                           final TaskConfigurationProperties taskConfigurationProperties) {
        return new ProjectService(projectRepository, taskGroupRepository, taskConfigurationProperties);
    }
}
