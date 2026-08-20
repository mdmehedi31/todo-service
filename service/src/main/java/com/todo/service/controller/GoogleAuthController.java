package com.todo.service.controller;

import com.todo.service.auth.google.GoogleAuthService;
import com.todom.library.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/oauth2")
public class GoogleAuthController {

    private static final Logger logger = LoggerFactory.getLogger(GoogleAuthController.class);
    private final GoogleAuthService googleAuthService;
    public GoogleAuthController(GoogleAuthService googleAuthService) {
        this.googleAuthService = googleAuthService;
    }

    @GetMapping("/google/connect-calender/{userId}")
    public ResponseEntity<ApiResponse> connectGoogleCalendar(@PathVariable Integer userId,
                                                             HttpServletRequest request) {
        System.out.println("Header value is: " + request.getHeader("Authorization"));
        ApiResponse response = this.googleAuthService.getAuthUrl(userId); //googleCalendarConnectionAuthUrl(userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/google/add-user-calendar-credential")
    public ResponseEntity<ApiResponse>
    saveUserGoogleCalendarConnectionCredential(@RequestParam(name = "code") String code,
                                                                                  @RequestParam(name = "userId") Integer userId,
                                                                                  @RequestParam(required = false) String email){
        ApiResponse response = this.googleAuthService.saveUserGoogleCalendarConnectionCredential(code, userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
