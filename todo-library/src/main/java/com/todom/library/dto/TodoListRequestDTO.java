package com.todom.library.dto;

import java.time.Instant;

public class TodoListRequestDTO extends AbstractIdAndUserTimeZone{

    private Integer todoId;
    private String title;
    private Integer createdBy;
    private String todoBgColor;
    private Integer sortOrder;
    private boolean isDeleted;
    private Boolean isDone;
    private Instant createdOn;

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

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public Boolean getIsDone() {
        return isDone;
    }

    public void setIsDone(Boolean done) {
        isDone = done;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }
}
