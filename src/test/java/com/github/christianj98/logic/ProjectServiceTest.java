package com.github.christianj98.logic;

import com.github.christianj98.TaskConfigurationProperties;
import com.github.christianj98.model.ProjectRepository;
import com.github.christianj98.model.TaskGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.assertj.core.api.AssertionsForClassTypes.*;
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

    @Test
    @DisplayName("should throw IllegalStateException when configured to allow just 1 group and the other undone group")
    public void createGroup_noMultipleGroupsConfig_And_openGroups_throwsIllegalStateException() {
        // given
        when(taskGroupRepository.existsByDoneIsFalseAndProject_Id(anyInt())).thenReturn(true);
        when(taskConfigurationProperties.getTemplate()).thenReturn(template);
        when(template.isAllowMultipleTasks()).thenReturn(false);

        var toTest = new ProjectService(null, taskGroupRepository, taskConfigurationProperties);

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
        var toTest = new ProjectService(projectRepository, taskGroupRepository, taskConfigurationProperties);

        // then
        assertThatThrownBy(() -> toTest.createGroup(0, LocalDateTime.now()))
                .isInstanceOf(EntityNotFoundException.class);
    }

}
