package com.github.christianj98.reports;

import com.github.christianj98.model.event.TaskDone;
import com.github.christianj98.model.event.TaskUndone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class ChangedTakEventListener {
    private static final Logger logger = LoggerFactory.getLogger(ChangedTakEventListener.class);

    @EventListener
    public void on(TaskDone event) {
        logger.info("Got " + event);
    }

    @EventListener
    public void on(TaskUndone event) {
        logger.info("Got " + event);
    }
}
