package com.todo.service.userservice;

import com.todom.library.dto.ApiResponse;
import com.todom.library.dto.UserDTO;
import com.todom.library.dto.UserRegistrationDTO;

public interface IUserService {

    public ApiResponse createUser(UserRegistrationDTO userRegistrationDTO);
    public UserDTO getUserByEmailOrUserId(String email, Integer userId);
    public UserDTO updateUser(UserRegistrationDTO userRegistrationDTO );

}
