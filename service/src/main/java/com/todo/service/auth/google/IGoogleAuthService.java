package com.todo.service.auth.google;

import com.todom.library.dto.ApiResponse;
import com.todom.library.dto.UserDTO;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

public interface IGoogleAuthService {

    public ApiResponse saveUserGoogleCalendarConnectionCredential(String authCode, Integer userId);
    public UserDTO checkGoogleAccessTokenIsValidOrElseGetNewToken(Integer userId) throws IOException, GeneralSecurityException;
    public ApiResponse getAuthUrl(Integer userId);
}
