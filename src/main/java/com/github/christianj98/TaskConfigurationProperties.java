package com.github.christianj98;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "task")
public class TaskConfigurationProperties {
    private boolean allowMultipleTasksFromTemplate;

    public boolean isAllowMultipleTasksFromTemplate() {
        return allowMultipleTasksFromTemplate;
    }

    public void setAllowMultipleTasksFromTemplate(final boolean allowMultipleTasksFromTemplate) {
        this.allowMultipleTasksFromTemplate = allowMultipleTasksFromTemplate;
    }
}
