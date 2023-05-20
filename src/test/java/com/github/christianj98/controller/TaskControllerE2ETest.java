package com.github.christianj98.controller;

import com.github.christianj98.model.Task;
import com.github.christianj98.model.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

// Annotation run server on random port
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskControllerE2ETest {

    private static final String URI = "/tasks";
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private TaskRepository taskRepository;

    @Test
    public void httpGet_returnsAllTasks() {
        // given
        final int initialSize = taskRepository.findAll().size();
        taskRepository.save(new Task("foo", LocalDateTime.now()));
        taskRepository.save(new Task("bar", LocalDateTime.now()));


        // when
        final Task[] result = testRestTemplate.getForObject("http://localhost:" + port + "/tasks", Task[].class);

        System.out.println(Arrays.toString(result));

        // then
        assertThat(result).hasSize(initialSize + 2);
    }

    @Test
    public void shouldCreateTask() {
        // given
        String description = "Test Task";
        Task task = new Task();
        task.setDescription(description);

        // when
        ResponseEntity<Task> response = testRestTemplate.postForEntity(createURLWithPort(URI), task, Task.class);
        Task createdTask = response.getBody();

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getLocation()).isNotNull();
        assertThat(response.getHeaders().getLocation().getPath()).contains(URI);
        assertThat(createdTask).isNotNull();
        assertThat(createdTask.getDescription()).isEqualTo(description);
    }

    @Test
    public void shouldUpdateTask() {
        Task taskToUpdate = new Task();
        taskToUpdate.setDescription("Initial task");
        Task createdTask = taskRepository.save(taskToUpdate);

        Task updatedTask = new Task();
        String updatedDescription = "Updated Task";
        updatedTask.setDescription(updatedDescription);

        ResponseEntity<Void> response = testRestTemplate.exchange(
                createURLWithPort(URI + "/" + createdTask.getId()),
                HttpMethod.PUT,
                new HttpEntity<>(updatedTask),
                Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        Task fetchedTask = taskRepository.findById(createdTask.getId()).orElse(null);
        assertThat(fetchedTask).isNotNull();
        assertThat(fetchedTask.getDescription()).isEqualTo(updatedDescription);
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
