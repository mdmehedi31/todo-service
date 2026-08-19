package com.todo.service.userservice;

import com.todom.library.dto.*;

public interface IUserService {

    public UserAuthInfo createUser(UserRegistrationDTO userRegistrationDTO);
    public UserDTO getUserByEmailOrUserId(String email, Integer userId);
    public UserDTO updateUser(UserRegistrationDTO userRegistrationDTO );
    public UserAuthInfo loginUser(UserLoginDTO userLoginDTO);
}
