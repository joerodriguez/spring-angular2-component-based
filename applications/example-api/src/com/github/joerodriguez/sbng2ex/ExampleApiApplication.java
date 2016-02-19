package com.github.joerodriguez.sbng2ex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class ExampleApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExampleApiApplication.class, args);
    }
}
