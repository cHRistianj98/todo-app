package com.github.christianj98.reports;

import com.github.christianj98.model.event.TaskEvent;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Table("task_events")
public class PersistedTaskEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    int taskId;
    LocalDateTime occurrence;
    String name;

    public PersistedTaskEvent() {
    }

    public PersistedTaskEvent(TaskEvent source) {
        taskId = source.getTaskId();
        name = source.getClass().getSimpleName();
        occurrence = LocalDateTime.ofInstant(source.getOccurence(), ZoneId.systemDefault());
    }
}
