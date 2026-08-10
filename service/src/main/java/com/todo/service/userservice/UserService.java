package com.todo.service.userservice;

import com.todo.core.dbservice.IUserDBService;
import com.todo.core.entity.UserEntity;
import com.todo.core.exceptionhandler.InvalidInputException;
import com.todo.core.exceptionhandler.NotFoundException;
import com.todo.core.impDbService.UserDBService;
import com.todo.core.repository.IUserRepository;
import com.todo.service.config.security.JwtAuthService;
import com.todom.library.dto.*;
import com.todom.library.enums.SuccessOrFailedEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserService implements IUserService {


    private final UserDBService userDBService;
    private IUserDBService iUserDBService;
    private PasswordEncoder passwordEncoder;
    private JwtAuthService jwtAuthService;
    private AuthenticationManager authenticationManager;
    public UserService(IUserDBService iUserDBService, PasswordEncoder passwordEncoder,
                       UserDBService userDBService, JwtAuthService jwtAuthService,
                       AuthenticationManager authenticationManager) {
        this.iUserDBService = iUserDBService;
        this.passwordEncoder = passwordEncoder;
        this.userDBService = userDBService;
        this.jwtAuthService = jwtAuthService;
        this.authenticationManager = authenticationManager;
    }
    private final Logger log = LoggerFactory.getLogger(UserService.class);

    @Override
    public ApiResponse createUser(UserRegistrationDTO userRegistrationDTO) {
        try{
            log.info("From service createUser");
            if(userRegistrationDTO==null){
                throw new InvalidInputException("User registration info can't be null");
            } else if (userRegistrationDTO.getEmail()==null) {
                throw new InvalidInputException("Email can't be null");
            }
        ApiResponse apiResponse = new ApiResponse();
        UserEntity userEntity=   iUserDBService.getUserByEmail(userRegistrationDTO.getEmail());
            if(userEntity !=null && userEntity.getId() !=null){
                apiResponse.setCustomMessage("User already exist with this email");
                return apiResponse;
            }
            UserEntity newUserEntity = new UserEntity();
            newUserEntity.setCreatedOn(Instant.now());

            String activationCode= getRandomVerificationCode();
            newUserEntity.setActivatedCode(activationCode);
            newUserEntity.setExpiresdAt(LocalDateTime.now().plusMinutes(10));
           UserEntity convertedUserEntity=userDTOToEntity(userRegistrationDTO,newUserEntity);
           UserEntity saveResponse= this.iUserDBService.createOrUpdateUser(convertedUserEntity);

            log.debug("From service createUser--> added user id is : " + saveResponse.getId());
            if(saveResponse!=null){
                Map<String, Object> map = new HashMap<>();
                map.put("id", saveResponse.getId());
                apiResponse.setOther(map);
                apiResponse.setMessage(SuccessOrFailedEnum.SUCCESS);
            }else{
                apiResponse.setMessage(SuccessOrFailedEnum.FAILED);
            }
            return apiResponse;
        }catch (InvalidInputException e){
            log.error("An exception thrown when creating a new user", e);
            return new ApiResponse(SuccessOrFailedEnum.FAILED);
        } catch (Exception e){
            log.error("An exception thrown when creating a new user", e);
            return new ApiResponse(SuccessOrFailedEnum.FAILED);
        }

    }

    private String getRandomVerificationCode(){
        SecureRandom random = new SecureRandom();
        Set<Integer> digits = new HashSet<>();

        while (digits.size() < 6) {
            digits.add(random.nextInt(10));
        }

        StringBuilder otp = new StringBuilder();
        for (int d : digits) {
            otp.append(d);
        }

        String sixDigitCode = otp.toString();

        return sixDigitCode;
    }

    @Override
    public UserDTO getUserByEmailOrUserId(String email, Integer userId) {
        try{
            UserEntity userEntity=null;
            if(userId != null){
                userEntity = iUserDBService.getUserByID(userId);
            }else{
                userEntity= iUserDBService.getUserByEmail(email);
            }

            if(userEntity==null){
                throw new NotFoundException("User not found");
            }

            UserDTO convertedUserDto= userEntityToDTO(userEntity);
            return convertedUserDto;
        }catch (NotFoundException e){
            log.error("An exception thrown : ",e);
           throw e;
        }
        catch (Exception e) {
            log.error("An exception thrown when getting user by email", e);
            //return new UserDTO();
            throw e;
        }
    }

    @Override
    public UserDTO updateUser(UserRegistrationDTO userRegistrationDTO) {
        try {
            if (userRegistrationDTO == null || userRegistrationDTO.getId() == null) {
                throw new InvalidInputException("User registration info can't be null");
            }
            UserEntity userEntity = iUserDBService.getUserByID(userRegistrationDTO.getId());

            if (userEntity == null || userEntity.getId() == null) {
                throw new NotFoundException("User not found with this id");
            }
            UserEntity convertedUserEntity = userDTOToEntity(userRegistrationDTO, userEntity);

            if (convertedUserEntity != null) {
                UserEntity updateUserEntity = this.iUserDBService.createOrUpdateUser(convertedUserEntity);
                UserDTO convertedUserDto = userEntityToDTO(updateUserEntity);
                return convertedUserDto;
            }
            return new UserDTO();
        } catch (NotFoundException e) {
            log.error("An exception thrown : ", e);
           // return new UserDTO();
            throw e;
        } catch (Exception e) {
            log.error("An exception thrown when updating user", e);
            throw e;
        }
    }

    @Override
    public UserAuthInfo loginUser(UserLoginDTO userLoginDto) {
        UserAuthInfo userAuthInfo= new UserAuthInfo();
        UserDTO userDTO=new UserDTO();

        try{
            if(userLoginDto==null || userLoginDto.getEmail()==null){
                userAuthInfo.setMessage("User registration info invalid");
                return userAuthInfo;
            }


          //  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginDto.getEmail(), userLoginDto.getPassword()));
            UserEntity userEntity=userDBService.getUserByEmail(userLoginDto.getEmail());

            if(userEntity==null){
                userAuthInfo.setMessage("User not found with this email");
                return userAuthInfo;
            }


            String endCodedPassword=passwordEncoder.encode(userLoginDto.getPassword());


            log.info("Endcoding password : " + endCodedPassword+", request pass : " +userEntity.getPassword());

           boolean isMatchPassword = passwordEncoder.matches(userLoginDto.getPassword(),userEntity.getPassword());
            if(!isMatchPassword){
                userAuthInfo.setMessage("Password does not match");
                return userAuthInfo;
            }

            userDTO.setId(userEntity.getId());
            userDTO.setEmail(userEntity.getEmail());

            if(userEntity.getFullName() !=null) {
                userDTO.setFullName(userEntity.getFullName());
            }

            if(userEntity.getPhoneNo()!=null) {
                userDTO.setPhoneNo(userEntity.getPhoneNo());
            }

            Map<String, String> jwt =jwtAuthService.getJwtToken(userLoginDto);
           // userAuthInfo.setJwtToken(jwt.get("token"));
            userAuthInfo.setJwtTokenValue(jwt);
            userAuthInfo.setUser(userDTO);
            return userAuthInfo;
        }catch (Exception e){
            log.error("An exception thrown : ",e);
         throw e;
        }
    }

    private UserEntity userDTOToEntity(UserRegistrationDTO userDTO, UserEntity userEntity) {

        if(userDTO != null){
            if ( userDTO.getFullName() != null) {
                userEntity.setFullName(userDTO.getFullName());
            }
            if (userDTO.getEmail() != null) {
                userEntity.setEmail(userDTO.getEmail());
            }
            if (userDTO.getPhoneNo() != null) {
                userEntity.setPhoneNo(userDTO.getPhoneNo());
            }
            if ( userDTO.getPassword() != null) {

                userEntity.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            }

            if (userDTO.getImage() != null) {
                userEntity.setImage(userDTO.getImage());
            }

            if(userDTO.getGoogleAccessToken() !=null ){
                userEntity.setGoogleAccessToken(userDTO.getGoogleAccessToken());
            }
            if(userDTO.getGoogleRefreshToken() !=null){
                userEntity.setGoogleResfreshToken(userDTO.getGoogleRefreshToken());
            }

            if(userDTO.getGoogleTokenExpiredAt() != null){
                userEntity.setGoogleTokenExpiresAt(userDTO.getGoogleTokenExpiredAt());
            }
            return userEntity;
        }
        return null;
    }

    private UserDTO userEntityToDTO(UserEntity userEntity) {
        UserDTO userDTO = new UserDTO();

        if (userEntity != null && userEntity.getId() != null) {
            userDTO.setId(userEntity.getId());
        }
        if (userEntity != null && userEntity.getFullName() != null) {
            userDTO.setFullName(userEntity.getFullName());
        }
        if (userEntity != null && userEntity.getEmail() != null) {
            userDTO.setEmail(userEntity.getEmail());
        }
        if (userEntity != null && userEntity.getPhoneNo() != null) {
            userDTO.setPhoneNo(userEntity.getPhoneNo());
        }
        if (userEntity != null && userEntity.getCreatedOn() != null) {
            userDTO.setCreatedOn(userEntity.getCreatedOn());
        }

        if(userEntity != null && userEntity.getGoogleAccessToken() != null){
            userDTO.setGoogleAccessToken(userEntity.getGoogleAccessToken());
        }

        if(userEntity !=null && userEntity.getGoogleResfreshToken() !=null){
            userDTO.setGoogleRefreshToken(userEntity.getGoogleResfreshToken());
        }

        if(userEntity !=null && userEntity.getGoogleTokenExpiresAt() != null){
            userDTO.setGoogleTokenExpiredAt(userEntity.getGoogleTokenExpiresAt());
        }
        userDTO.setIsBlocked(userEntity.getIsBlocked());

        return userDTO;
    }
}
