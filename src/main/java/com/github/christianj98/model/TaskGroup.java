package com.github.christianj98.model;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "task_groups")
public class TaskGroup extends AbstractTaskBase {

    @Embedded
    private Audit audit = new Audit();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "group")
    private Set<Task> tasks;

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(final Set<Task> tasks) {
        this.tasks = tasks;
    }

    public void updateFrom(final TaskGroup source) {
        this.setDescription(source.getDescription());
        this.setDone(source.isDone());
    }
}
