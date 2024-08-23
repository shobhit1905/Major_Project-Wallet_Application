package com.majorproject.wallet_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.majorproject.jbdl_wallet_library.DTO.SendMailNotification;
import com.majorproject.jbdl_wallet_library.constants.TopicConstants;
import com.majorproject.jbdl_wallet_library.enums.ServiceType;
import com.majorproject.user_service.model.User;
import com.majorproject.user_service.service.UserService;
import com.majorproject.wallet_service.entity.Wallet;
import com.majorproject.wallet_service.repository.WalletRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

@Slf4j
@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepository ;

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper ;

    private UserService userService ;


    public void createWalletForNewUser(Map<String,String> receivedData) throws JsonProcessingException {
        Long userId = Long.parseLong(receivedData.get("userId"));
        Wallet wallet = new Wallet(userId) ;
        log.info(String.format("Wallet for user : %d , created successfully\n" +
                "Details of the wallet are : %s" , userId , wallet.toString())) ;

        Wallet w = walletRepository.save(wallet) ;

        log.info(String.format("Saved wallet details are : %s" , w.toString())) ;

        SendMailNotification sendMailNotification = SendMailNotification.builder()
                .receiverMailId("shobhitnautiyal979665@gmail.com")
                .message(String.format("Hi %s \n Your wallet account is created \n " +
                        "Now you have access to all the features\n" +
                        "Your daily limit is 100000 \n " +
                        "Your daily transaction limit is 10 \n" +
                        "You can update this anytime you want" , receivedData.get("userName")))
                .serviceType(ServiceType.WALLET_SERVICE)
                .subject("New wallet onboarding")
                .build() ;

        Future<SendResult<String, String>> send;
        send = kafkaTemplate.send(TopicConstants.SEND_NOTIFICATION_TOPIC,
                w.getUserId().toString() ,
                objectMapper.writeValueAsString(sendMailNotification));
    }

    public String getWalletDetailsForUser(Long userId){
        Wallet wallet = walletRepository.findByUserId(userId) ;
        if(wallet == null)
            return null ;

        return wallet.toString() ;
    }

    public List<Wallet> getAllWalletDetails() {
        return walletRepository.findAll();
    }

    public Wallet getWalletDetailsWithId(Long walletId){
        return walletRepository.findById(walletId).orElse(null) ;
    }

    public Wallet getWalletUsingUserName(String userName){
        User user = userService.getUserByName(userName) ;
        if(user == null)
            return null ;
        Long userId = user.getUserId() ;
        return walletRepository.findByUserId(userId) ;
    }
}
