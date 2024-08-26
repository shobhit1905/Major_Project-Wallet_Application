package com.majorproject.wallet_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.majorproject.jbdl_wallet_library.DTO.*;
import com.majorproject.jbdl_wallet_library.constants.TopicConstants;
import com.majorproject.jbdl_wallet_library.enums.PaymentStatus;
import com.majorproject.jbdl_wallet_library.enums.ServiceType;
import com.majorproject.notification_service.templates.MailsTemplates;
import com.majorproject.user_service.model.User;
import com.majorproject.user_service.service.UserService;
import com.majorproject.wallet_service.DTO.UpdateBalanceDTO;
import com.majorproject.wallet_service.DTO.UpdateMoneyLimit;
import com.majorproject.wallet_service.entity.Wallet;
import com.majorproject.wallet_service.repository.WalletRepository;
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
public class WalletService {

    @Autowired
    private WalletRepository walletRepository ;

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper ;

    @Autowired
    private RestTemplate restTemplate ;


    public void createWalletForNewUser(UserWalletCreationRequest receivedData) throws JsonProcessingException {
        Long userId = receivedData.getUserId();
        Wallet wallet = new Wallet(userId) ;
        log.info(String.format("Wallet for user : %d , created successfully\n" +
                "Details of the wallet are : %s" , userId , wallet.toString())) ;

        Wallet newCreatedWallet = walletRepository.save(wallet) ;

        log.info("Saved wallet details for userId : %d \n are : %s" , userId, newCreatedWallet) ;

        SendMailNotification sendMailNotification = SendMailNotification.builder()
                .receiverMailId(receivedData.getUserEmailId())
                .message(String.format(MailsTemplates.getWalletCreationbody().getMailBody() , receivedData.getUserName()))
                .serviceType(ServiceType.WALLET_SERVICE)
                .subject(MailsTemplates.getWalletCreationbody().getMailSubject())
                .build() ;

        Future<SendResult<String, String>> send;
        send = kafkaTemplate.send(TopicConstants.SEND_NOTIFICATION_TOPIC,
                newCreatedWallet.getUserId().toString() ,
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

//    public Wallet getWalletUsingUserName(String userName){
//        User user = userService.getUserByName(userName) ;
//        if(user == null)
//            return null ;
//        Long userId = user.getUserId() ;
//        return walletRepository.findByUserId(userId) ;
//    }

    public Double getUserBalance(Long userId){
        User user = restTemplate.getForEntity("http://localhost:8081/wallet-user/" + userId, User.class).getBody() ;
        if(user == null)
            return null ;

        Wallet wallet = walletRepository.findByUserId(userId) ;
        return wallet.getWalletBalance() ;
    }

    public void updateBalanceForUsers(InitiateTransactionDTO receivedData) throws JsonProcessingException {
        Long senderId = receivedData.getSenderID() ;
        Long receiverId = receivedData.getReceiverId();

        Wallet sender = walletRepository.findByUserId(senderId) ;
        Wallet receiver = walletRepository.findByUserId(receiverId) ;

        sender.setWalletBalance(sender.getWalletBalance() - receivedData.getAmount()) ;
        receiver.setWalletBalance(receiver.getWalletBalance() + receivedData.getAmount()) ;

        walletRepository.save(sender) ;
        walletRepository.save(receiver) ;

        SuccessfulTransactionDTO successfulTransactionDTO = SuccessfulTransactionDTO.builder()
                .transactionID(receivedData.getTransactionId())
                .paymentStatus(PaymentStatus.SUCCESSFUL)
                .build() ;

        Future<SendResult<String, String>> send = kafkaTemplate.send(TopicConstants.SUCCESSFUL_TRANSACTION_TOPIC, receivedData.getTransactionId()
                , objectMapper.writeValueAsString(successfulTransactionDTO)) ;
    }

    public Boolean deleteWalletById(Long walletId){
        Wallet wallet = walletRepository.findById(walletId).orElse(null) ;
        if(wallet == null)
            return false ;

        walletRepository.delete(wallet) ;
        return true ;
    }

    public WalletDeleteDTO getWalletDeleteDTO(Long userId){
        Wallet wallet = walletRepository.findByUserId(userId) ;
        if(wallet == null)
            return null ;

        return WalletDeleteDTO.builder()
                .walletId(wallet.getWalletId())
                .userId(userId)
                .build();

    }

    public Boolean updateDailyMoneyLimit(UpdateMoneyLimit dto){
        User user = restTemplate.getForEntity("http://localhost:8081/wallet-user/" + dto.getUserId(), User.class).getBody() ;
        if(user == null)
            return null ;

        Wallet w = walletRepository.findByUserId(dto.getUserId()) ;
        w.setDailyLimit(dto.getNewLimit());
        walletRepository.save(w) ;
        return true ;
    }

    public Boolean updateWalletBalance(UpdateBalanceDTO updateBalanceDTO) {
        try{
            User user = restTemplate.getForEntity("http://localhost:8081/wallet-user/" + updateBalanceDTO.getUserId(), User.class).getBody();
            if(user == null)
                return false ;

            Wallet w = walletRepository.findByUserId(updateBalanceDTO.getUserId()) ;
            w.setWalletBalance(w.getWalletBalance() + updateBalanceDTO.getAmount()) ;
            walletRepository.save(w) ;
            return true ;
        }
        catch(Exception e){
            return false ;
        }

    }
}
