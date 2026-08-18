package com.todom.library.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TodoResponseDTO extends TodoRequestDTO{

    private Instant createdOn;
    private Integer sortIndex;
    private Boolean isDeleted;
    private Integer totalTodoItems;
    private Integer totalDoneItems;

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

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public Integer getTotalTodoItems() {
        return totalTodoItems;
    }

    public void setTotalTodoItems(Integer totalTodoItems) {
        this.totalTodoItems = totalTodoItems;
    }

    public Integer getTotalDoneItems() {
        return totalDoneItems;
    }

    public void setTotalDoneItems(Integer totalDoneItems) {
        this.totalDoneItems = totalDoneItems;
    }
}
