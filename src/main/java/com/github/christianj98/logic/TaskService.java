package com.github.christianj98.logic;

import com.github.christianj98.adapter.SqlTaskRepository;
import com.github.christianj98.model.Task;
import com.github.christianj98.model.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class TaskService {
    private final static Logger LOGGER = LoggerFactory.getLogger(TaskService.class);
    private final TaskRepository taskRepository;

    public TaskService(final SqlTaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Async
    public CompletableFuture<List<Task>> findAllAsync() {
        LOGGER.info("Supply async!");
        return CompletableFuture.supplyAsync(taskRepository::findAll);
    }
}
