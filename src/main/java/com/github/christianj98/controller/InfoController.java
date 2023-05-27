package com.github.christianj98.controller;

import com.github.christianj98.TaskConfigurationProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/info")
public class InfoController {

    private final DataSourceProperties dataSourceProperties;
    private final TaskConfigurationProperties taskConfigurationProperties;

    public InfoController(final DataSourceProperties dataSourceProperties,
                          final TaskConfigurationProperties taskConfigurationProperties) {
        this.dataSourceProperties = dataSourceProperties;
        this.taskConfigurationProperties = taskConfigurationProperties;
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/url")
    public String url() {
        return dataSourceProperties.getUrl();
    }

    @RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping("/prop")
    public boolean getProp() {
        return taskConfigurationProperties.getTemplate().isAllowMultipleTasks();
    }

}
