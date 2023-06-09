package com.github.christianj98.logic;

import com.github.christianj98.model.Project;
import com.github.christianj98.model.TaskGroup;
import com.github.christianj98.model.TaskGroupRepository;
import com.github.christianj98.model.TaskRepository;
import com.github.christianj98.model.projection.GroupReadModel;
import com.github.christianj98.model.projection.GroupWriteModel;

import java.util.List;
import java.util.stream.Collectors;

// @Service -> Defined in LogicConfiguration class
public class TaskGroupService {

    private final TaskGroupRepository taskGroupRepository;
    private final TaskRepository taskRepository;

    public TaskGroupService(final TaskGroupRepository taskGroupRepository,
                            final TaskRepository taskRepository) {
        this.taskGroupRepository = taskGroupRepository;
        this.taskRepository = taskRepository;
    }

    public GroupReadModel createGroup(GroupWriteModel source) {
        return createGroup(source, null);
    }
    public GroupReadModel createGroup(final GroupWriteModel source, final Project project) {
        TaskGroup result = taskGroupRepository.save(source.toGroup(project));
        return new GroupReadModel(result);
    }

    public List<GroupReadModel> readAll() {
        return taskGroupRepository.findAll().stream()
                .map(GroupReadModel::new)
                .collect(Collectors.toList());
    }

    public void toggleGroup(int groupId) {
        if (taskRepository.existsByDoneIsFalseAndGroup_Id(groupId)) {
            throw new IllegalStateException("Group has undone tasks. Done all the tasks first");
        }
        TaskGroup result = taskGroupRepository.findById(groupId).orElseThrow(() ->
                new IllegalArgumentException("TaskGroup with given id not found"));
        result.setDone(!result.isDone());
        taskGroupRepository.save(result);
    }


}
