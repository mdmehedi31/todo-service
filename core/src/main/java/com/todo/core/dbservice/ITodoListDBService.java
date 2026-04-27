package com.todo.core.dbservice;

import com.todo.core.entity.TodoListEntity;
import com.todom.library.dto.TodoListResponseDTO;

import java.util.List;

public interface ITodoListDBService {

    public TodoListEntity createTodoList(TodoListEntity todoListEntity);
    public TodoListEntity updateTodoList(TodoListEntity todoListEntity);
    public TodoListEntity deleteTodoList(TodoListEntity todoListEntity);
    public List<TodoListResponseDTO> getTodoListByTodoId(Integer todoId);
    public List<TodoListEntity> getTodoListByTodoListId(Integer todoListId);
    public List<TodoListEntity> getTodoLists();
    public List<TodoListEntity> getTodoListByTodoIdAndTitle(Integer todoListId, String title);
    public TodoListEntity getTodoListById(Integer todoListId);
    public boolean deleteTodoListByTodoId(Integer todoId);
}
