package com.github.christianj98;

import com.github.christianj98.model.Task;
import com.github.christianj98.model.TaskGroup;
import com.github.christianj98.model.TaskGroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class Warmup implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(Warmup.class);

    private final TaskGroupRepository taskGroupRepository;

    public Warmup(final TaskGroupRepository taskGroupRepository) {
        this.taskGroupRepository = taskGroupRepository;
    }

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        logger.info("Application warmup after context refreshed");
        final String description = "ApplicationContextEvent";
        if (!taskGroupRepository.existsByDescription(description)) {
            logger.info("No required group found! Adding it!");
            var group = new TaskGroup();
            group.setDescription(description);
            group.setTasks(Set.of(
                    new Task("CpntextClosedEvent", null),
                    new Task("CpntextRefreshedEvent", null),
                    new Task("CpntextStoppedEvent", null),
                    new Task("CpntextStartedEvent", null)
            ));
            taskGroupRepository.save(group);
        }
    }
}
