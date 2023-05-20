package com.github.christianj98.controller;

import com.github.christianj98.model.Task;
import com.github.christianj98.model.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

// Annotation run server on random port
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskControllerE2ETest {

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
}
