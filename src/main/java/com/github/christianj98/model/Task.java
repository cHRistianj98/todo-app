package com.github.christianj98.model;

import com.github.christianj98.model.event.TaskEvent;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
public class Task extends AbstractTaskBase {

    private LocalDateTime deadline;

    @Embedded
    private Audit audit = new Audit();

    @ManyToOne
    @JoinColumn(name = "task_group_id")
    private TaskGroup group;

    public Task() {
    }

    public Task(String description, LocalDateTime deadline) {
        this.setDescription(description);
        this.deadline = deadline;
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

    public TaskEvent toggle() {
        this.setDone(!this.isDone());
        return TaskEvent.changed(this);
    }

    public void updateFrom(final Task source) {
        this.setDescription(source.getDescription());
        this.setDone(source.isDone());
        deadline = source.getDeadline();
        group = source.group;
    }

    @Override
    public String toString() {
        return "Task{" +
                "deadline=" + deadline +
                ", audit=" + audit +
                ", group=" + group +
                ", id=" + getId() +
                ", description=" + getDescription() +
                ", done=" + isDone() +
                "}";
    }
}
