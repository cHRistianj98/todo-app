package com.github.christianj98.controller;

import com.github.christianj98.model.projection.ProjectWriteModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Project controller is responsible for returning template
 */
@Controller
@RequestMapping("/projects")
public class ProjectController {

    @GetMapping
    public String showProjects(Model model) {
        model.addAttribute("project", new ProjectWriteModel());

        // Spring tries to find template with name projects.html, if it will find then the view will be rendered in the browser
        return "projects";
    }

}
