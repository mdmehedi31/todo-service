package com.todo.core.impDbService;

import com.todo.core.dbservice.IUserDBService;
import com.todo.core.entity.UserEntity;
import com.todo.core.repository.IUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;


@Service
public class UserDBService implements IUserDBService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final Logger log = LoggerFactory.getLogger(UserDBService.class);

    @Override
    public UserEntity createOrUpdateUser(UserEntity userSaveDataEntity) {
        try {
            UserEntity userEntitySaveResponse = this.userRepository.save(userSaveDataEntity);
            return userEntitySaveResponse;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public UserEntity getUserByEmail(String email) {
        try {
            UserEntity userEntity = this.userRepository.findUserEntityByEmail(email);
            return userEntity;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public UserEntity getUserByID(int id) {
        try {
            UserEntity userEntity = this.userRepository.findUserEntityById(id);
            return userEntity;
        } catch (Exception e) {
            throw e;
        }
    }

}
