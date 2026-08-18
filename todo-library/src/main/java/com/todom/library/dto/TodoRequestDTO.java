package com.todom.library.dto;

import com.todom.library.enums.TodoType;

import java.time.Instant;

public class TodoRequestDTO extends AbstractIdAndUserTimeZone {

    private String title;
    private TodoType todoType;
    private Instant goalStartDate;
    private Instant goalEndDate;
    private Integer createdById;
    private Integer totalTodoListItem;
    private Integer progress;
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public TodoType getTodoType() {
        return todoType;
    }

    public void setTodoType(TodoType todoType) {
        this.todoType = todoType;
    }

    public Instant getGoalStartDate() {
        return goalStartDate;
    }

    public void setGoalStartDate(Instant goalStartDate) {
        this.goalStartDate = goalStartDate;
    }

    public Instant getGoalEndDate() {
        return goalEndDate;
    }

    public void setGoalEndDate(Instant goalEndDate) {
        this.goalEndDate = goalEndDate;
    }

    public Integer getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Integer createdById) {
        this.createdById = createdById;
    }

    public Integer getTotalTodoListItem() {
        return totalTodoListItem;
    }

    public void setTotalTodoListItem(Integer totalTodoListItem) {
        this.totalTodoListItem = totalTodoListItem;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }
}
