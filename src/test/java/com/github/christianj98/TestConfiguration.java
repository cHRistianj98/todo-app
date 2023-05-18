package com.github.christianj98;

import com.github.christianj98.model.TaskRepository;
import com.github.christianj98.model.TaskRepositoryTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class TestConfiguration {
    @Bean
    @Profile({"integration", "!staging"}) // bean will be wired only if profile is "integration"
        // @ConditionalOnMissingBean
        // @Primary
    TaskRepository testRepo() {
        return new TaskRepositoryTest();
    }
}
