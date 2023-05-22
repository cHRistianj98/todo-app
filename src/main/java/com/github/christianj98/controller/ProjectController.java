package com.github.christianj98.controller;

import com.github.christianj98.logic.ProjectService;
import com.github.christianj98.model.Project;
import com.github.christianj98.model.ProjectStep;
import com.github.christianj98.model.projection.ProjectWriteModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Project controller is responsible for returning template
 */
@Controller
@RequestMapping("/projects")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(final ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public String showProjects(Model model) {
        model.addAttribute("project", new ProjectWriteModel());

        // Spring tries to find template with name projects.html, if it will find then the view will be rendered in the browser
        return "projects";
    }

    @PostMapping
    public String addProject(@ModelAttribute("project") ProjectWriteModel current, Model model) {
        projectService.createProject(current);
        model.addAttribute("project", new ProjectWriteModel());
        model.addAttribute("message", "Dodano project!");
        return "projects";
    }

    @PostMapping(params = "addStep")
    // @ModelAttribute -> attribute is from model "project"
    public String addProjectStep(@ModelAttribute("project") ProjectWriteModel current) {
        current.getSteps().add(new ProjectStep());
        return "projects";
    }

    @ModelAttribute("projects")
    public List<Project> getProjects() {
        return projectService.readAll();
    }

}
