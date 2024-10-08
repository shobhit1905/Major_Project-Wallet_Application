package com.majorproject.user_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.majorproject.jbdl_wallet_library.DTO.UserWalletCreationRequest;
import com.majorproject.jbdl_wallet_library.DTO.WalletDeleteDTO;
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
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
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

    @Autowired
    private RestTemplate restTemplate;

    public UserResponseDTO createNewUser(User user) throws JsonProcessingException {
        User newUser = userRepository.save(user) ;
        log.info(String.format("User account is created for UserId: %d and UserName: %s", newUser.getUserId(),newUser.getUserFullName()));

        UserWalletCreationRequest userWalletCreationRequest = UserWalletCreationRequest.builder()
                .userId(newUser.getUserId())
                .userName(newUser.getUserFullName())
                .userEmailId(newUser.getUserEmail())
                .build() ;

        Future<SendResult<String, String>> send = kafkaTemplate.send(TopicConstants.USER_CREATION_TOPIC
        , newUser.getUserId().toString() , objectMapper.writeValueAsString(userWalletCreationRequest)) ;
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

        WalletDeleteDTO dto = restTemplate.getForObject("http://localhost:8091/wallet-wallet/getWallet/user/" + id, WalletDeleteDTO.class);
        if(dto == null)
            throw new RuntimeException("Not able to delete wallet information , please try after sometime") ;

        Boolean walletDeleted = restTemplate.getForObject("http://localhost:8091/wallet-wallet/deleteWallet/" + dto.getWalletId(), Boolean.class);

        if(Boolean.TRUE.equals(walletDeleted)){
            log.info("Wallet deleted successfully , now proceeding to delete user data");
            userRepository.deleteById(id);
            return true ;
        }
        else{
            throw new RuntimeException("Not able to delete wallet information , please try after sometime") ;
        }
    }

    public User getUserByName(String userName){
        return userRepository.findUserByName(userName);
    }

    public Boolean checkForUserExistence(Long userId) {
        return userRepository.existsById(userId) ;
    }
}
