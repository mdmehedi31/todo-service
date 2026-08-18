package com.todom.library.dto;

import java.util.List;

public class DashBoardDTO {

    private Integer totalInprogress;
    private Integer totalDone;
    private Integer overDue;
    private Integer totalDoneItemPercentage;
    private List<TodoResponseDTO> recentTodos;
    private List<TodoResponseDTO> overDueTodos;


    private List<TodoResponseDTO>  todoResponseList;

    public List<TodoResponseDTO> getTodoResponseList() {
        return todoResponseList;
    }

    public void setTodoResponseList(List<TodoResponseDTO> todoResponseList) {
        this.todoResponseList = todoResponseList;
    }

    public Integer getTotalInprogress() {
        return totalInprogress;
    }

    public void setTotalInprogress(Integer totalInprogress) {
        this.totalInprogress = totalInprogress;
    }

    public Integer getTotalDone() {
        return totalDone;
    }

    public void setTotalDone(Integer totalDone) {
        this.totalDone = totalDone;
    }

    public Integer getOverDue() {
        return overDue;
    }

    public void setOverDue(Integer overDue) {
        this.overDue = overDue;
    }

    public List<TodoResponseDTO> getRecentTodos() {
        return recentTodos;
    }

    public void setRecentTodos(List<TodoResponseDTO> recentTodos) {
        this.recentTodos = recentTodos;
    }

    public Integer getTotalDoneItemPercentage() {
        return totalDoneItemPercentage;
    }

    public void setTotalDoneItemPercentage(Integer totalDoneItemPercentage) {
        this.totalDoneItemPercentage = totalDoneItemPercentage;
    }

    public List<TodoResponseDTO> getOverDueTodos() {
        return overDueTodos;
    }

    public void setOverDueTodos(List<TodoResponseDTO> overDueTodos) {
        this.overDueTodos = overDueTodos;
    }
}
