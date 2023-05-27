package com.github.christianj98.model.event;

import com.github.christianj98.model.Task;

import java.time.Clock;

public class TaskUndone extends TaskEvent{
    public TaskUndone(final Task source) {
        super(source.getId(), Clock.systemDefaultZone());
    }
}
