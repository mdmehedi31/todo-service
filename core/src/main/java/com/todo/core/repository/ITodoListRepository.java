package com.todo.core.repository;

import com.todo.core.entity.TodoListEntity;
import com.todo.core.utils.DBUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ITodoListRepository extends JpaRepository<TodoListEntity, Integer> {
    List<TodoListEntity> getTodoListEntityByTodoIdAndTitle(Integer todoId, String title);

    List<TodoListEntity> getTodoListEntityByTodoIdAndTitleAndIsDeleted(Integer todoListId, String title, boolean isDeleted);

    @Query(value = "SELECT t FROM "+ DBUtils.TB_TODO_LIST +" t WHERE t.id=:todoListId AND (t.isDeleted IS NULL or t.isDeleted = false)")
    TodoListEntity findByIdAndIsDeleted(@Param("todoListId") Integer todoListId);

    List<TodoListEntity> findAllByTodoId(Integer todoId);


}
