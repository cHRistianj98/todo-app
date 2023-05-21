package com.github.christianj98.controller;

import com.github.christianj98.adapter.SqlTaskRepository;
import com.github.christianj98.logic.TaskGroupService;
import com.github.christianj98.model.TaskGroupRepository;
import com.github.christianj98.model.TaskRepository;
import com.github.christianj98.model.projection.GroupReadModel;
import com.github.christianj98.model.projection.GroupTaskReadModel;
import com.github.christianj98.model.projection.GroupWriteModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/groups")
public class TaskGroupController {
    private static final Logger logger = LoggerFactory.getLogger(TaskGroupController.class);

    private final TaskGroupService taskGroupService;

    private final TaskGroupRepository taskGroupRepository;
    private final TaskRepository taskRepository;

    public TaskGroupController(final TaskGroupService taskGroupService,
                               final TaskGroupRepository taskGroupRepository,
                               final SqlTaskRepository taskRepository) {
        this.taskGroupService = taskGroupService;
        this.taskGroupRepository = taskGroupRepository;
        this.taskRepository = taskRepository;
    }

    @PostMapping
    public ResponseEntity<GroupReadModel> createGroup(@RequestBody @Valid final GroupWriteModel source) {
        logger.info("create group");
        final GroupReadModel createdGroup = taskGroupService.createGroup(source);
        String id = Integer.toString(createdGroup.getId());
        String pathToResource = "/groups/{id}";
        String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(pathToResource)
                .buildAndExpand(id)
                .toUriString();
        return ResponseEntity.created(URI.create(uri)).body(createdGroup);
    }

    @GetMapping
    public ResponseEntity<List<GroupReadModel>> readAllGroups() {
        logger.info("read all groups");
        return ResponseEntity.ok(taskGroupService.readAll());
    }

    @PatchMapping("/{groupId}")
    public ResponseEntity<?> toggleGroup(@PathVariable final int groupId) {
        logger.info("toggle group");
        if (taskGroupRepository.findById(groupId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        taskGroupService.toggleGroup(groupId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/tasks")
    public ResponseEntity<List<GroupTaskReadModel>> readAllTaskWithinGroup(@PathVariable final int id) {
        return ResponseEntity.ok(taskRepository.findByGroup_Id(id).stream()
                .map(GroupTaskReadModel::new)
                .collect(Collectors.toList()));
    }

    /*
        These exception handlers are invoked if exceptions will be thrown
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalState(IllegalStateException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
