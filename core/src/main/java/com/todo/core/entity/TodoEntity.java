package com.todo.core.entity;

import com.todo.core.utils.DBUtils;
import com.todom.library.enums.TodoType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.Instant;

@Entity(name = DBUtils.TB_TODO)
public class TodoEntity extends AbstractIdEntity{

    @Column(name="todo_title")
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name="todo_type")
    private TodoType todoType;

    @Column(name="goal_start_date")
    private Instant goalStartDate;

    @Column(name="goal_end_date")
    private Instant goalEndDate;

    @Column(name="created_by_id")
    private Integer createdById;

    @Column(name="created_on")
    private Instant createdOn;

    @Column(name="sort_index")
    private Integer sortIndex;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

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

    public Instant getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public Integer getSortIndex() {
        return sortIndex;
    }

    public void setSortIndex(Integer sortIndex) {
        this.sortIndex = sortIndex;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}
