package com.todo.service.ai;

import com.todom.library.dto.TodoDTO;

public interface ITodoGeneratorService {


    public TodoDTO generateTodoByTopics(String message);

}
