package com.todo.core.repository;

import com.todo.core.entity.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ITodoRepository extends JpaRepository<TodoEntity, Integer> {
    TodoEntity findByTitle(String title);

    List<TodoEntity> findAllByTitle(String title);

    List<TodoEntity> findAllByCreatedById(Integer createdById);

    List<TodoEntity> findAllByTitleAndCreatedById(String title, Integer createdById);

    Optional<TodoEntity> findByIdAndIsDeleted(Integer todoId, boolean isDeleted);
}
