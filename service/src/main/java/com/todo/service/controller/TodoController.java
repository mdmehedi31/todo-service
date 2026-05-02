package com.todo.service.controller;


import com.todo.service.todoservice.ITodoService;
import com.todom.library.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todo")
public class TodoController {

    @Autowired
    private ITodoService todoService;
    // Todo API
    @PostMapping("/create")
    public ResponseEntity<TodoResponseDTO> createTodo(@RequestBody TodoRequestDTO todoRequestDTO) {
        TodoResponseDTO todoResponseDTO = todoService.createTodo(todoRequestDTO);
        return new ResponseEntity<>(todoResponseDTO, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<TodoResponseDTO>  updateTodo(@RequestBody TodoRequestDTO todoRequestDTO) {
        TodoResponseDTO todoResponseDTO = todoService.updateTodo(todoRequestDTO);
        return new ResponseEntity<>(todoResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/get/{userId}")
    public ResponseEntity<List<TodoResponseDTO>> getAllTodosByUser(@PathVariable Integer userId) {
        List<TodoResponseDTO> todoList = todoService.getAllTodoByCreatedById(userId);
        return new ResponseEntity<>(todoList, HttpStatus.OK);
    }

    @PutMapping("/delete/{todoId}")
    public ResponseEntity<ApiResponse> deleteTodoByTodoId(@PathVariable Integer todoId) {
        ApiResponse apiResponse = todoService.deleteTodo(todoId);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    //Todo List Api
    @PostMapping("/create-todolist")
    public ResponseEntity<TodoListResponseDTO> createTodoList(@RequestBody TodoListRequestDTO todoListRequestDTO) {
        TodoListResponseDTO todoListResponseDTO = todoService.createTodoList(todoListRequestDTO);
        return  new ResponseEntity<>(todoListResponseDTO, HttpStatus.CREATED);
    }

    @PutMapping("/update-todolist")
    public ResponseEntity<TodoListResponseDTO> updateTodoList(@RequestBody TodoListRequestDTO todoListRequestDTO) {
        TodoListResponseDTO todoListResponseDTO= todoService.updateTodoList(todoListRequestDTO);
        return  new ResponseEntity<>(todoListResponseDTO, HttpStatus.OK);
    }

    @PutMapping("/delete-todolist/{todolistId}")
    public ResponseEntity<ApiResponse> deleteTodoList(@PathVariable Integer todolistId) {
        ApiResponse response= todoService.deleteTodoList(todolistId);
        return  new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/get-todolist/{todoId}")
    public ResponseEntity<List<TodoListResponseDTO>> getTodoList(@PathVariable Integer todoId) {
        List<TodoListResponseDTO> todoListResponse = this.todoService.getAllTodoListByTodoId(todoId);
        return  new ResponseEntity<>(todoListResponse, HttpStatus.OK);
    }

    @PostMapping("/add-ai-generated-todo/{createdById}")
    public ResponseEntity<ApiResponse> addAiGeneratedTodo(@RequestBody TodoDTO todoDTO, @PathVariable Integer createdById) {
        ApiResponse response = this.todoService.addAiGeneratedTodoList(todoDTO,createdById);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
