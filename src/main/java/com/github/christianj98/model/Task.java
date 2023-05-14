package com.github.christianj98.model;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Task's description must not be empty")
    private String description;

    private boolean done;

    private LocalDateTime deadline;

    @Embedded
    private Audit audit = new Audit();

    @ManyToOne
    @JoinColumn(name = "task_group_id")
    private TaskGroup group;

    public Task() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(final LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public TaskGroup getGroup() {
        return group;
    }

    public void setGroup(final TaskGroup group) {
        this.group = group;
    }

    public void updateFrom(final Task source) {
        description = source.getDescription();
        done = source.isDone();
        deadline = source.getDeadline();
        group = source.group;
    }
}
