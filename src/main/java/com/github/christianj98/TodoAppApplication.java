package com.github.christianj98;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.Validator;


@SpringBootApplication
@ConfigurationPropertiesScan
public class TodoAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodoAppApplication.class, args);
	}

	@Bean
	public Validator validator() {
		return new LocalValidatorFactoryBean();
	}

}
