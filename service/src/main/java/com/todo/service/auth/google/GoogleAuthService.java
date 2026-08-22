package com.todo.service.auth.google;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.todo.core.entity.UserEntity;
import com.todo.core.impDbService.UserDBService;
import com.todo.service.userservice.IUserService;
import com.todom.library.dto.ApiResponse;
import com.todom.library.dto.UserDTO;
import com.todom.library.enums.SuccessOrFailedEnum;
import com.todom.library.utils.DateTimeUtil;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.util.*;

@Service
public class GoogleAuthService implements IGoogleAuthService{

    private static final Logger log= LoggerFactory.getLogger(GoogleAuthService.class);

    @Value("${google.client_id}")
    private String clientId;
    @Value("${google.secret_id}")
    private String clientSecret;
    @Value("${google.redirect_uri}")
    private String redirectUri;

    @Value("${google.google_scopes}")
    private String scopes;

    @Autowired
    private IUserService userService;

    @Autowired
    private UserDBService userDBService;

 /*   private GoogleAuthorizationCodeFlow buildFlow() {
        try{
            String clientSecrateJson=String.format("{\"web\":{\"client_id\":\"%s\",\"client_secret\":\"%s\"}}",clientId,clientSecret);
            log.info("clientSecrateJson value is = "+clientSecrateJson);
            GoogleClientSecrets secrets= GoogleClientSecrets.load(GsonFactory.getDefaultInstance(), new StringReader(clientSecrateJson));
            return new GoogleAuthorizationCodeFlow.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance(),
                    secrets,
                    Collections.singleton(CalendarScopes.CALENDAR)).setAccessType("offline").build();
        }catch (Exception e){
            log.error("An exception occurred while trying to build a Flow", e);
            return null;
        }
    }*/


    public ApiResponse getAuthUrl(Integer userId){
        try{

            ApiResponse response = new ApiResponse();
            UserDTO userDTO =userService.getUserByEmailOrUserId(null, userId);
            String state="";

            if(userDTO !=null && userDTO.getId() !=null) {
                state = "{\"userId\":" + userDTO.getId() + ",\"email\":\"" + userDTO.getEmail() + "\"}";
                state= Base64.getUrlEncoder().withoutPadding().encodeToString(state.getBytes(StandardCharsets.UTF_8));
            }

            String [] scopeList = scopes.split(" ");
          GoogleAuthorizationCodeRequestUrl authRequst = new GoogleAuthorizationCodeRequestUrl(clientId,redirectUri,Arrays.asList(scopeList));

          if(!state.isBlank()) {
              authRequst.setState(state);
          }
          authRequst.setAccessType("offline");
          String url= authRequst.build();
            Map<String,Object> redirectUrlMap=new HashMap<>();
            redirectUrlMap.put("url",url);
            log.info("New method -> redirect url is = "+url);
            response.setOther(redirectUrlMap);
            return response;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Boolean isGoogleCalendarConnected(Integer userId) {
        try{
            Boolean isConnected = false;
            UserEntity user=userDBService.getUserByID(userId);
            if(user !=null && user.getGoogleAccessToken() !=null && user.getGoogleResfreshToken() !=null) {
                isConnected = true;
            }
            return isConnected;
        }catch (Exception e){
            log.error(e.getMessage());
            return false;
        }
    }

    @Override
    public ApiResponse saveUserGoogleCalendarConnectionCredential(String authCode, Integer userId) {
        try{
            GoogleTokenResponse tokenRequest =
                    new GoogleAuthorizationCodeTokenRequest(GoogleNetHttpTransport.newTrustedTransport(),
                            GsonFactory.getDefaultInstance(),clientId,clientSecret,authCode,redirectUri).execute();
            ApiResponse addResponse = new ApiResponse();
            if(tokenRequest!=null){
                String accessToken = tokenRequest.getAccessToken();
                String refreshToken = tokenRequest.getRefreshToken();
                Long expiredAt = tokenRequest.getExpiresInSeconds();
                UserDTO user = this.userService.getUserByEmailOrUserId(null, userId);
                if(user!=null) {
                    UserDTO updateResponse= this.saveOrUpdateGoogleCalendarCredential(accessToken,refreshToken,expiredAt, user);
                    if(updateResponse != null){
                        HashMap<String,Object> calendarConnectCredential=new HashMap<>();
                        calendarConnectCredential.put("accessToken",accessToken);
                        calendarConnectCredential.put("refreshToken",refreshToken);
                        calendarConnectCredential.put("expiredAt",expiredAt);
                        addResponse.setOther(calendarConnectCredential);
                        addResponse.setMessage(SuccessOrFailedEnum.SUCCESS);
                        return addResponse;
                    }else{
                        addResponse.setCustomMessage("User not exist with this id");
                    }
                }
            }
            ObjectMapper mapper= new ObjectMapper();
            mapper.writeValueAsString(addResponse);
            log.info("The converted value is = "+addResponse);
            addResponse.setMessage(SuccessOrFailedEnum.FAILED);
            return addResponse;
        }catch (Exception e){
             log.error("An exception thrown");
            return new ApiResponse("Get exception when trying to add user credential to db table");
        }
    }

    public UserDTO checkGoogleAccessTokenIsValidOrElseGetNewToken(Integer userId) throws IOException, GeneralSecurityException {
        try{
            UserDTO user = this.userService.getUserByEmailOrUserId(null, userId);
          //  log.info("Expired date : {}, Now time is : {}, Expired token value is : {} ",user.getGoogleTokenExpiredAt(), Instant.now(),user.getGoogleTokenExpiredAt().isBefore(Instant.now()));
            if(user!=null &&  user.getGoogleTokenExpiredAt() !=null && user.getGoogleTokenExpiredAt().isAfter(Instant.now())){
                log.info("When token is not expired : {}", user.getGoogleTokenExpiredAt());
                return user;
            }
            log.info("When token is expired....");
            user= this.refreshUserGoogleCalendarConnectionCredential(user);
            return user;
        } catch (IOException e) {
            throw e;
        }catch (Exception e){
            throw  e;
        }
    }

    private UserDTO saveOrUpdateGoogleCalendarCredential(String accessToken, String refreshToken, Long expiredAt, UserDTO userDTO) {
        try{

            Instant currentTime = Instant.now();
            Integer substractSecondValue =DateTimeUtil.SUBSTRACT_VALUE_FOR_GOOGLE_CALENDAR*60;
            log.info("Expired time before substraction : {}, sub value is : {}", expiredAt,substractSecondValue );
            expiredAt=expiredAt-substractSecondValue;
            log.info("Expired time after substraction : {}", expiredAt);
            currentTime=currentTime.plusSeconds(expiredAt);

            userDTO.setGoogleAccessToken(accessToken);
            if(refreshToken !=null){
                userDTO.setGoogleRefreshToken(refreshToken);
            }
            userDTO.setGoogleTokenExpiredAt(currentTime);
             userDTO=this.userService.updateUser(userDTO);
            return userDTO;
        }catch (Exception e){
            throw e;
        }
    }

    private UserDTO refreshUserGoogleCalendarConnectionCredential(UserDTO user) throws IOException, GeneralSecurityException {
        try{
            if(user !=null && user.getGoogleRefreshToken()!=null) {

                GoogleTokenResponse refreshTokenRequest = new GoogleRefreshTokenRequest(GoogleNetHttpTransport.newTrustedTransport(),
                        GsonFactory.getDefaultInstance(),user.getGoogleRefreshToken(),clientId,clientSecret).execute();

                log.info("The expired at value is : {} and date is : {}", refreshTokenRequest.getExpiresInSeconds(), refreshTokenRequest.getAccessToken());
                user = this.saveOrUpdateGoogleCalendarCredential(refreshTokenRequest.getAccessToken(), refreshTokenRequest.getRefreshToken(),refreshTokenRequest.getExpiresInSeconds(), user);
                return user;
            }
            return null;
        }
        catch (Exception e) {
            throw e;
        }
    }

}
