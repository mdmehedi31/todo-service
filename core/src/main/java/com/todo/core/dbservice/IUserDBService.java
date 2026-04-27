package com.todo.core.dbservice;

import com.todo.core.entity.UserEntity;

public interface IUserDBService {

    public UserEntity createOrUpdateUser(UserEntity userSaveDataEntity);
    public UserEntity getUserByEmail(String email);
    public UserEntity getUserByID(int id);

}
