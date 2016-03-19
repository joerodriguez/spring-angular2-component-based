package com.github.joerodriguez.sbng2ex;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.module.kotlin.KotlinModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class ExampleApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExampleApiApplication.class, args);
    }

    @Bean
    public Module parameterNamesModule() {
        return new KotlinModule();
    }
}
