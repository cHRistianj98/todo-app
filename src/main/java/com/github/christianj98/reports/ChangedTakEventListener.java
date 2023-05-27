package com.github.christianj98.reports;

import com.github.christianj98.model.event.TaskDone;
import com.github.christianj98.model.event.TaskEvent;
import com.github.christianj98.model.event.TaskUndone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class ChangedTakEventListener {
    private static final Logger logger = LoggerFactory.getLogger(ChangedTakEventListener.class);

    private final PersistedTaskEventRepository repository;

    public ChangedTakEventListener(final PersistedTaskEventRepository repository) {
        this.repository = repository;
    }

    @EventListener
    public void on(TaskDone event) {
        onChanged(event);
    }

    @EventListener
    public void on(TaskUndone event) {
        onChanged(event);
    }

    private void onChanged(final TaskEvent event) {
        logger.info("Got " + event);
        repository.save(new PersistedTaskEvent(event));
    }
}
