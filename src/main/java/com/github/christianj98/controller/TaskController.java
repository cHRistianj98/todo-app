package com.github.christianj98.controller;

import com.github.christianj98.adapter.SqlTaskRepository;
import com.github.christianj98.logic.TaskService;
import com.github.christianj98.model.Task;
import com.github.christianj98.model.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final TaskRepository repository;

    private final TaskService taskService;

    public TaskController(/* @Qualifier("sqlTaskRepository") */ /*@Lazy */ final SqlTaskRepository repository,
                                                                           final TaskService taskService) {
        this.repository = repository;
        this.taskService = taskService;
    }

    @GetMapping(params = {"!sort", "!page", "!size"})
    public CompletableFuture<ResponseEntity<List<Task>>> readAllTasks() {
        logger.warn("Exposing all the tasks!");
        return taskService.findAllAsync().thenApply(ResponseEntity::ok);
    }

    @GetMapping
    public ResponseEntity<List<Task>> readAllTasks(Pageable page) {
        logger.info("Custom pageable");
        return ResponseEntity.ok(repository.findAll(page).getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> readTask(@PathVariable int id) {
        logger.info("Read one task");
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(repository.findById(id).get());
    }

    @GetMapping("/search/done")
    ResponseEntity<List<Task>> readDoneTasks(@RequestParam(defaultValue = "true") boolean state) {
        return ResponseEntity.ok(repository.findByDone(state));
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody @Valid Task task) {
        logger.info("Create one task");
        // assumption: id = 0 does not exist and id = 0 if id was not specified in the request
        if (task.getId() != 0 && repository.existsById(task.getId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        Task createdTask = repository.save(task);
        String id = Integer.toString(createdTask.getId());
        String pathToResource = "/tasks/{id}";
        String uri = ServletUriComponentsBuilder.fromCurrentContextPath().path(pathToResource).buildAndExpand(id).toUriString();
        return ResponseEntity.created(URI.create(uri)).body(createdTask);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable int id, @RequestBody @Valid Task toUpdate) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.findById(id)
                .ifPresent(task -> {
                    task.updateFrom(toUpdate);
                    repository.save(task);
                });
        return ResponseEntity.noContent().build();
    }

    @Transactional
    @PatchMapping("/{id}")
    public ResponseEntity<?> toggleTask(@PathVariable int id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.findById(id).ifPresent(task -> task.setDone(!task.isDone()));
        return ResponseEntity.noContent().build();
    }

}
