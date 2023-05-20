package com.github.christianj98.controller;

import com.github.christianj98.model.AbstractTaskBase;
import com.github.christianj98.model.Task;
import com.github.christianj98.model.TaskRepository;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("integration")
@AutoConfigureMockMvc
public class TaskControllerIntegrationTest {

    public static final String URI = "/tasks";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Test
    public void httpGet_returnGivenTask() throws Exception {
        // given
        final int id = taskRepository.save(new Task("foo", LocalDateTime.now())).getId();

        // when + then
        mockMvc.perform(get("/tasks/" + id))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void shouldCreateTask() throws Exception {
        // given
        String description = "Task to create";
        JSONObject requestBody = new JSONObject();
        requestBody.put("description", description);

        // when
        mockMvc.perform(post(URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody.toString()))
                .andExpect(status().isCreated());

        // then
        final List<Task> tasks = taskRepository.findAll();
        assertThat(tasks).extracting(AbstractTaskBase::getDescription).contains(description);
    }

    @Test
    public void shouldUpdateTask() throws Exception {
        // given
        Task task = new Task();
        task.setDescription("Initial Task");
        Task savedTask = taskRepository.save(task);

        JSONObject requestBody = new JSONObject();
        requestBody.put("description", "Updated Task");

        // when
        mockMvc.perform(put("/tasks/{id}", savedTask.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody.toString()))
                .andExpect(status().isNoContent());

        // then
        Optional<Task> updatedTask = taskRepository.findById(savedTask.getId());
        assertTrue(updatedTask.isPresent());
        assertEquals("Updated Task", updatedTask.get().getDescription());
    }


}
