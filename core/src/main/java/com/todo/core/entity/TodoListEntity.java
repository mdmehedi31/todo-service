package com.todo.core.entity;

import com.todo.core.utils.DBUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity(name = DBUtils.TB_TODO_LIST)
public class TodoListEntity extends AbstractIdEntity{

    @Column(name = "todo_id")
    private Integer todoId;

    @Column(name = "title")
    private String title;

    @Column(name = "created_on")
    private Instant createdOn;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "todo_bg_color")
    private String todoBgColor;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    public Integer getTodoId() {
        return todoId;
    }

    public void setTodoId(Integer todoId) {
        this.todoId = todoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public String getTodoBgColor() {
        return todoBgColor;
    }

    public void setTodoBgColor(String todoBgColor) {
        this.todoBgColor = todoBgColor;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
