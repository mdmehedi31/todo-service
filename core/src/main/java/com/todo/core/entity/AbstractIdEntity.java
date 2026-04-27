package com.todo.core.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;

@MappedSuperclass
public class AbstractIdEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
