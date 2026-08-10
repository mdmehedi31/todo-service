package com.todo.service.controller;

import com.todo.service.userservice.IUserService;
import com.todom.library.dto.ApiResponse;
import com.todom.library.dto.UserDTO;
import com.todom.library.dto.UserRegistrationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "/user")
@RestController
public class UserController {

    @Autowired
    private IUserService userService;

    private final Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/get")
    public ResponseEntity<UserDTO> getUserByEmailOrUserId(@RequestParam(required = false) String email,
                                                          @RequestParam(required = false) Integer userId) {
        UserDTO userDTO=userService.getUserByEmailOrUserId(email, userId);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserRegistrationDTO userRegistrationDTO) {
        UserDTO userDTO=userService.updateUser(userRegistrationDTO);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

}
