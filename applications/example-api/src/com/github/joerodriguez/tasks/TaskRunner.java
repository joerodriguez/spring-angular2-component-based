package com.github.joerodriguez.tasks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan("com.github.joerodriguez")
public class TaskRunner {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(TaskRunner.class, args);

        AppTask task = (AppTask) ctx.getBean(args[0]);
        task.run();

        System.exit(0);
    }
}
