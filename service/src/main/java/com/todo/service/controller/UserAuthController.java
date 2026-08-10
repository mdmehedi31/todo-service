package com.todo.service.controller;


import com.todo.service.userservice.IUserService;
import com.todom.library.dto.ApiResponse;
import com.todom.library.dto.UserAuthInfo;
import com.todom.library.dto.UserLoginDTO;
import com.todom.library.dto.UserRegistrationDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class UserAuthController {

    private IUserService userService;

    public UserAuthController(IUserService userService) {
        this.userService = userService;
    }


    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createNewUser(@RequestBody UserRegistrationDTO userRegistrationDTO) {
        ApiResponse response = userService.createUser(userRegistrationDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserAuthInfo> login(@RequestBody UserLoginDTO userLoginDTO) {
        UserAuthInfo userAuthInfo = userService.loginUser(userLoginDTO);
        return new ResponseEntity<>(userAuthInfo, HttpStatus.OK);
    }
}
