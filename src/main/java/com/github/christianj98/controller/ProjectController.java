package com.github.christianj98.controller;

import com.github.christianj98.logic.ProjectService;
import com.github.christianj98.model.Project;
import com.github.christianj98.model.ProjectStep;
import com.github.christianj98.model.projection.ProjectWriteModel;
import io.micrometer.core.annotation.Timed;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Project controller is responsible for returning template
 */
@Controller
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/projects")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(final ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public String showProjects(Model model, Authentication auth) {
//        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
        model.addAttribute("project", new ProjectWriteModel());

        // Spring tries to find template with name projects.html, if it will find then the view will be rendered in the browser
        return "projects";
//        }
//        return "index";

    }

    @PostMapping
    public String addProject(@ModelAttribute("project") @Valid ProjectWriteModel current,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            return "projects";
        }
        projectService.createProject(current);
        model.addAttribute("project", new ProjectWriteModel());
        model.addAttribute("projects", getProjects());
        model.addAttribute("message", "Dodano project!");
        return "projects";
    }

    @PostMapping(params = "addStep")
    // @ModelAttribute -> attribute is from model "project"
    public String addProjectStep(@ModelAttribute("project") ProjectWriteModel current) {
        current.getSteps().add(new ProjectStep());
        return "projects";
    }

    // Counting time of the requests wi the help of an actuator
    @Timed(value = "project.create.group", histogram = true, percentiles = {0.5, 0.95, 0.99})
    @PostMapping("/{id}")
    public String createGroup(@ModelAttribute("project") ProjectWriteModel current,
                              Model model,
                              @PathVariable int id,
                              // specify format of the date
                              @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime deadline) {
        try {
            projectService.createGroup(id, deadline);
        } catch (IllegalStateException | IllegalArgumentException e) {
            model.addAttribute("message", "Błąd podczas tworzenia grupy!");
        }
        return "projects";
    }

    @ModelAttribute("projects")
    public List<Project> getProjects() {
        return projectService.readAll();
    }

}
