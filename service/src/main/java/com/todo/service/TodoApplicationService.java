package com.todo.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages ={"com.todom.library", "com.todo.core", "com.todo.service"})
public class TodoApplicationService {


    public static void main(String[] args) {
        SpringApplication.run(TodoApplicationService.class, args);
    }
}
