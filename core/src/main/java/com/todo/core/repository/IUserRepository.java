package com.todo.core.repository;

import com.todo.core.entity.UserEntity;
import com.todom.library.dto.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<UserEntity, Integer> {
    UserEntity getUserEntityByEmail(String email);

    UserEntity getUserEntityById(int id);

    UserEntity findUserEntityByEmail(String email);

    UserEntity findUserEntityById(Integer id);
}
