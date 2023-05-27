package com.github.christianj98.model.event;

import com.github.christianj98.model.Task;

import java.time.Clock;
import java.time.Instant;

public abstract class TaskEvent {
    // factory method
    public static TaskEvent changed(Task source) {
        return source.isDone() ? new TaskDone(source) : new TaskUndone(source);
    }
    private int taskId;

    private Instant occurence;

    public TaskEvent(final int taskId, final Clock clock) {
        this.taskId = taskId;
        this.occurence = Instant.now(clock);
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(final int taskId) {
        this.taskId = taskId;
    }

    public Instant getOccurence() {
        return occurence;
    }

    public void setOccurence(final Instant occurence) {
        this.occurence = occurence;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "taskId=" + taskId +
                ", occurence=" + occurence +
                '}';
    }
}
