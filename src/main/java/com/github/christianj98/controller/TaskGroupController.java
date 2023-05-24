package com.github.christianj98.controller;

import com.github.christianj98.adapter.SqlTaskRepository;
import com.github.christianj98.logic.TaskGroupService;
import com.github.christianj98.model.TaskGroupRepository;
import com.github.christianj98.model.TaskRepository;
import com.github.christianj98.model.projection.GroupReadModel;
import com.github.christianj98.model.projection.GroupTaskReadModel;
import com.github.christianj98.model.projection.GroupTaskWriteModel;
import com.github.christianj98.model.projection.GroupWriteModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@IllegalExceptionProcessing
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

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    public String showGroups(Model model) {
        model.addAttribute("group", new GroupWriteModel());
        return "groups";
    }


    @PostMapping(produces = MediaType.TEXT_HTML_VALUE, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String addGroup(@ModelAttribute("group") @Valid GroupWriteModel current,
                           BindingResult bindingResult,
                           Model model) {
        if (bindingResult.hasErrors()) {
            return "groups";
        }
        taskGroupService.createGroup(current);
        model.addAttribute("group", new GroupWriteModel());
        model.addAttribute("groups", getGroups());
        model.addAttribute("message", "Dodano grupę!");
        return "groups";
    }

    @PostMapping(params = "addTask", produces = MediaType.TEXT_HTML_VALUE)
    // @ModelAttribute -> attribute is from model "project"
    public String addGroupTask(@ModelAttribute("group") GroupWriteModel current) {
        current.getTasks().add(new GroupTaskWriteModel());
        return "groups";
    }

    @ResponseBody
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
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

    @ResponseBody
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GroupReadModel>> readAllGroups() {
        logger.info("read all groups");
        return ResponseEntity.ok(taskGroupService.readAll());
    }

    @ResponseBody
    @PatchMapping("/{groupId}")
    public ResponseEntity<?> toggleGroup(@PathVariable final int groupId) {
        logger.info("toggle group");
        if (taskGroupRepository.findById(groupId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        taskGroupService.toggleGroup(groupId);
        return ResponseEntity.noContent().build();
    }

    @ResponseBody
    @GetMapping(value = "/{id}/tasks", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GroupTaskReadModel>> readAllTaskWithinGroup(@PathVariable final int id) {
        return ResponseEntity.ok(taskRepository.findByGroup_Id(id).stream()
                .map(GroupTaskReadModel::new)
                .collect(Collectors.toList()));
    }

    @ModelAttribute("groups")
    private List<GroupReadModel> getGroups() {
        return taskGroupService.readAll();
    }
}
