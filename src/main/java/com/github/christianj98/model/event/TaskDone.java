package com.github.christianj98.model.event;

import com.github.christianj98.model.Task;

import java.time.Clock;

public class TaskDone extends TaskEvent {
    public TaskDone(final Task source) {
        super(source.getId(), Clock.systemDefaultZone());
    }
}
