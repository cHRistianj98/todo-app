package com.github.christianj98;

import com.github.christianj98.model.TaskRepository;
import com.github.christianj98.model.TaskRepositoryTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class TestConfiguration {

    @Bean
    @Primary
    @Profile("!integration")
    public DataSource e2eTestDataSource() {
        var result = new DriverManagerDataSource("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "");
        result.setDriverClassName("org.h2.Driver");
        return result;
    }

    @Bean
    @Primary
    @Profile("integration") // bean will be wired only if profile is "integration"
        // @ConditionalOnMissingBean
        // @Primary
    public TaskRepository testRepo() {
        return new TaskRepositoryTest();
    }
}
