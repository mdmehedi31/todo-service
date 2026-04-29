package com.todo.service.controller;


import com.todo.service.ai.ITodoGeneratorService;
import com.todom.library.dto.TodoDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("ai")
public class AITodoController {


    private ITodoGeneratorService todoGeneratorService;

    public AITodoController(ITodoGeneratorService todoGeneratorService) {
        this.todoGeneratorService = todoGeneratorService;
    }


    @GetMapping("/generate-todo-list")
    public ResponseEntity<TodoDTO> generateTodo(@RequestParam("message") String message ) {
        TodoDTO todoDTO = this.todoGeneratorService.generateTodoByTopics(message);
        return ResponseEntity.ok(todoDTO);
    }
}
