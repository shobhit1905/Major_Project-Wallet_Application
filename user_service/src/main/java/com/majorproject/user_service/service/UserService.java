package com.majorproject.user_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.majorproject.jbdl_wallet_library.constants.TopicConstants;
import com.majorproject.user_service.DTO.UpdateUserAddressDTO;
import com.majorproject.user_service.DTO.UpdateUserEmail;
import com.majorproject.user_service.DTO.UpdateUserMobileDTO;
import com.majorproject.user_service.DTO.UserResponseDTO;
import com.majorproject.user_service.model.User;
import com.majorproject.user_service.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public UserResponseDTO createNewUser(User user) throws JsonProcessingException {
        User newUser = userRepository.save(user) ;
        Map<String,String> walletCreationRequest = Map.of("userId" , newUser.getUserId().toString() , "userName" , newUser.getUserFullName()) ;
        String message  = String.format("Account created for user\nName : %s\nId : %s" , newUser.getUserFullName(),newUser.getUserId()) ;
        log.info(message) ;
        Future<SendResult<String, String>> send = kafkaTemplate.send(TopicConstants.USER_CREATION_TOPIC
        , newUser.getUserId().toString() , objectMapper.writeValueAsString(walletCreationRequest)) ;
        return UserResponseDTO.builder()
                .message("User account created , Wallet creation in progress , we will notify you over mail once its done")
                .user(newUser)
                .build();
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User getUserById(Long userId){
        return userRepository.findById(userId).orElse(null);

    }

    public Boolean updateUserMobile(UpdateUserMobileDTO user){
        User u = userRepository.findById(user.getUserId()).orElse(null);
        if(u == null)
            return false;

        u.setUserMobile(user.getNewMobile());
        userRepository.save(u);
        return true ;
    }

    public Boolean updateUserAddress(UpdateUserAddressDTO user){
        User u = userRepository.findById(user.getUserId()).orElse(null);
        if(u == null)
            return false ;

        u.setUserAddress(user.getNewAddress());
        userRepository.save(u);
        return true ;
    }

    public Boolean updateUserEmail(UpdateUserEmail user){
        User u = userRepository.findById(user.getUserId()).orElse(null ) ;
        if(u == null)
            return false ;

        u.setUserEmail(user.getNewEmail());
        userRepository.save(u) ;
        return true ;
    }

    public Boolean deleteUserById(Long id){
        User u = userRepository.findById(id).orElse(null);
        if(u == null)
            return false ;

        userRepository.delete(u);
        return true ;
    }

}
