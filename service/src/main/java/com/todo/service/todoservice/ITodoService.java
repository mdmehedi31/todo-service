package com.todo.service.todoservice;

import com.todom.library.dto.*;

import java.util.List;

public interface ITodoService {
    //TOdO CRUD
    public TodoResponseDTO createTodo(TodoRequestDTO todoRequestDTO);
    public List<TodoResponseDTO> getAllTodoByCreatedById(Integer createdById);
    public TodoResponseDTO updateTodo(TodoRequestDTO todoRequestDTO);
    public ApiResponse deleteTodo(Integer todoId);

    // TODOD List CRUE
    public TodoListRequestDTO createTodoList(TodoListRequestDTO todoListRequestDTO);
    public TodoListRequestDTO updateTodoList(TodoListRequestDTO todoListRequestDTO);
    public ApiResponse deleteTodoList(Integer todoListId);
    public List<TodoListRequestDTO> getAllTodoListByTodoId(Integer todoId);

    public ApiResponse addAiGeneratedTodoList(TodoDTO todoDTO, Integer createdById);

}
