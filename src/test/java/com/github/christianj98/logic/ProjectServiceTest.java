package com.github.christianj98.logic;

import com.github.christianj98.TaskConfigurationProperties;
import com.github.christianj98.model.Project;
import com.github.christianj98.model.ProjectRepository;
import com.github.christianj98.model.ProjectStep;
import com.github.christianj98.model.TaskGroup;
import com.github.christianj98.model.TaskGroupRepository;
import com.github.christianj98.model.projection.GroupReadModel;
import com.github.christianj98.model.projection.GroupTaskReadModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    @Mock
    private TaskGroupRepository taskGroupRepository;

    @Mock
    private TaskConfigurationProperties taskConfigurationProperties;

    @Mock
    private TaskConfigurationProperties.Template template;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private TaskGroupService taskGroupService;

    @Test
    @DisplayName("should throw IllegalStateException when configured to allow just 1 group and the other undone group")
    public void createGroup_noMultipleGroupsConfig_And_openGroups_throwsIllegalStateException() {
        // given
        when(taskGroupRepository.existsByDoneIsFalseAndProject_Id(anyInt())).thenReturn(true);
        when(taskConfigurationProperties.getTemplate()).thenReturn(template);
        when(template.isAllowMultipleTasks()).thenReturn(false);

        var toTest = new ProjectService(null, taskGroupRepository, taskGroupService, taskConfigurationProperties);

        // when
        var exception = catchThrowable(() -> toTest.createGroup(0, LocalDateTime.now()));

        // then
        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("one group is");
        assertThatThrownBy(() -> toTest.createGroup(0, LocalDateTime.now()))
                .isInstanceOf(IllegalStateException.class);
        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> toTest.createGroup(0, LocalDateTime.now()));
        assertThatIllegalStateException()
                .isThrownBy(() -> toTest.createGroup(0, LocalDateTime.now()));
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when configuration ok and no projects for a given id")
    public void createGroup_configurationOk_And_noProjects_throwsIllegalArgumentException() {

        // given
        when(taskGroupRepository.existsByDoneIsFalseAndProject_Id(anyInt())).thenReturn(true);
        when(taskConfigurationProperties.getTemplate()).thenReturn(template);
        when(template.isAllowMultipleTasks()).thenReturn(true);
        when(projectRepository.findById(anyInt())).thenReturn(Optional.empty());

        // when
        var toTest = new ProjectService(projectRepository, taskGroupRepository, taskGroupService, taskConfigurationProperties);

        // then
        assertThatThrownBy(() -> toTest.createGroup(0, LocalDateTime.now()))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("should create a new group from project")
    public void createGroup_configurationOk_existingProject_createsAndSavesGroup() {
        // given
        LocalDateTime today = LocalDate.now().atStartOfDay();
        String projectStepDescription = "project step description";
        Project project = createProject(projectStepDescription);

        when(projectRepository.findById(anyInt())).thenReturn(Optional.of(project));

        var toTest = new ProjectService(projectRepository, taskGroupRepository, taskGroupService, taskConfigurationProperties);

        // when
        GroupReadModel groupModel = toTest.createGroup(1, today);

        // then
        assertThat(groupModel.getDeadline()).isEqualTo(LocalDate.now().atStartOfDay().plusDays(2));
        assertThat(groupModel.getDescription()).isEqualTo(project.getDescription());
        assertThat(groupModel.getTasks()).extracting(GroupTaskReadModel::getDescription)
                .containsOnly(projectStepDescription);
    }

    private Project createProject(String projectStepDescription) {
        final Project project = new Project();
        project.setId(1);
        project.setDescription("project description");
        project.setSteps(Set.of(createProjectStep(projectStepDescription)));
        return project;
    }

    private ProjectStep createProjectStep(String projectStepDescription) {
        final ProjectStep projectStep = new ProjectStep();
        projectStep.setId(1);
        projectStep.setDescription(projectStepDescription);
        projectStep.setDaysToDeadline(2);
        return projectStep;
    }

    // Instructor example
    private static class InMemoryGroupRepository implements TaskGroupRepository {
        private int index = 0;
        private Map<Integer, TaskGroup> map = new HashMap<>();

        public int count() {
           return map.values().size();
        }

        @Override
        public List<TaskGroup> findAll() {
            return new ArrayList<>(map.values());
        }

        @Override
        public Optional<TaskGroup> findById(final Integer id) {
            return Optional.ofNullable(map.get(id));
        }

        @Override
        public TaskGroup save(final TaskGroup entity) {
            if (entity.getId() == 0) {
                try {
                    TaskGroup.class.getDeclaredField("id").set(entity, ++index);
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    throw new RuntimeException(e);
                }
            } else {
                map.put(entity.getId(), entity);
            }
            return entity;
        }

        @Override
        public boolean existsByDoneIsFalseAndProject_Id(final Integer projectId) {
            return map.values().stream()
                    .filter(group -> !group.isDone())
                    .anyMatch(group -> group.getProject() != null && group.getProject().getId() == projectId);
        }
    }

}
