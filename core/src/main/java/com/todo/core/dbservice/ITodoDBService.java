package com.todo.core.dbservice;


import com.todo.core.entity.TodoEntity;
import com.todo.core.entity.TodoListEntity;
import com.todom.library.dto.DashBoardDTO;
import com.todom.library.dto.TodoResponseDTO;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public interface ITodoDBService {
    // Todo related method
    public List<TodoEntity> getTodoByTitleAndCreatedById(String title, Integer createdBy);
    public TodoEntity getTodoById(Integer todoId);
    public List<TodoEntity> getAllTodoByUserId(Integer userId);
    public TodoEntity addTodo(TodoEntity todoEntity);
    public List<TodoResponseDTO> getAllTodosByCreator(Integer userId);
    public DashBoardDTO getDashBoardValue(LocalDate goalStartDate, LocalDate goalEndDate);
}
