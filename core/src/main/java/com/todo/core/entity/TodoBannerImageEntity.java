package com.todo.core.entity;

import com.todo.core.utils.DBUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity(name = DBUtils.TB_TODO_BANNER)
public class TodoBannerImageEntity extends AbstractIdEntity{

    @Column(name = "todo_id")
    private Integer todoId;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "image")
    private byte[] image;


    public Integer getTodoId() {
        return todoId;
    }

    public void setTodoId(Integer todoId) {
        this.todoId = todoId;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
