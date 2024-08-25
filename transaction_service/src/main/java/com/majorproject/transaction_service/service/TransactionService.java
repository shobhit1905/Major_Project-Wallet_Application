package com.majorproject.transaction_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.majorproject.jbdl_wallet_library.DTO.InitiateTransactionDTO;
import com.majorproject.jbdl_wallet_library.DTO.SendMailNotification;
import com.majorproject.jbdl_wallet_library.DTO.SuccessfulTransactionDTO;
import com.majorproject.jbdl_wallet_library.constants.TopicConstants;
import com.majorproject.transaction_service.DTO.UserTransactionRequest;
import com.majorproject.transaction_service.DTO.UserTransactionResponse;
import com.majorproject.jbdl_wallet_library.enums.PaymentStatus;
import com.majorproject.transaction_service.entity.Transaction;
import com.majorproject.transaction_service.repository.TransactionRepository;
import com.majorproject.user_service.model.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.naming.factory.SendMailFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

@Service
@Slf4j
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private RestTemplate restTemplate ;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public UserTransactionResponse createTransactionRequest(UserTransactionRequest request) throws JsonProcessingException {
        Long senderId = request.getSenderId();
        Boolean senderExistCheck = restTemplate.getForObject("http://localhost:8081/wallet-user/userExistence/" + senderId, Boolean.class);
        if(Boolean.FALSE.equals(senderExistCheck)){
            log.info("Sender with is : {} do not exist", request.getSenderId());
            throw new RuntimeException(String.format("Sender with is : %s do not exist", request.getSenderId())) ;
        }

        Long receiverId = request.getReceiverId();
        Boolean receiverExistCheck = restTemplate.getForObject("http://localhost:8081/wallet-user/userExistence/" + receiverId, Boolean.class);
        if(Boolean.FALSE.equals(receiverExistCheck)){
            log.info("Receiver with is : {} do not exist", request.getReceiverId());
            throw new RuntimeException(String.format("Receiver with is : %s do not exist", request.getReceiverId())) ;
        }

        Double availableBalance = restTemplate.getForObject("http://localhost:8091/wallet-wallet/balance/fetchBalance/" + senderId, Double.class);
        if(availableBalance < request.getAmount()){
            log.error("Insufficient balance , Account balance : {} ", availableBalance);
            throw new RuntimeException(String.format("Insufficient balance , Account balance : %s ", availableBalance));
        }

        Transaction currentTransaction = new Transaction(request.getWalletId(), request.getSenderId(), request.getReceiverId(), request.getAmount());
        Transaction savedTransaction = transactionRepository.save(currentTransaction);

        InitiateTransactionDTO initiateTransactionDTORequest = InitiateTransactionDTO.builder()
                .transactionId(savedTransaction.getTransactionId())
                .senderID(savedTransaction.getSenderId())
                .walletID(savedTransaction.getWalletId())
                .receiverId(savedTransaction.getReceiverId())
                .amount(savedTransaction.getAmount())
                .build() ;

        Future<SendResult<String, String>> send = kafkaTemplate.send(TopicConstants.INITIATE_TRANSACTION_TOPIC
                , savedTransaction.getTransactionId() , objectMapper. writeValueAsString(initiateTransactionDTORequest)) ;

        return UserTransactionResponse.builder()
                .transactionId(savedTransaction.getTransactionId())
                .message("Your transaction is in progress. Please wait")
                .paymentStatus(PaymentStatus.PENDING)
                .transactionInitiationTime(savedTransaction.getTransactionInitiationDate())
                .build() ;

        // after this push the message to the kafka.
        // Kafka will actually update the wallet by calling the wallet services

    }

    public void updateTransactionStatus(SuccessfulTransactionDTO receivedData) {
        Transaction transaction = transactionRepository.findById(receivedData.getTransactionID()).get() ;

        transaction.setPaymentStatus(
                receivedData.getPaymentStatus().equals(PaymentStatus.SUCCESSFUL) ? PaymentStatus.SUCCESSFUL : PaymentStatus.FAILED
        );

        transactionRepository.save(transaction);
    }

//    public void sendUserNotification(SuccessfulTransactionDTO receivedData) {
//
//        Transaction transaction = transactionRepository.findById(receivedData.getTransactionID()).get() ;
//
//        Long senderId = transaction.getSenderId();
//        Long receiverId = transaction.getReceiverId();
//
//        User sender = restTemplate.getForEntity("http://localhost:8081/wallet-user/" + senderId, User.class).getBody();
//        User receiver = restTemplate.getForEntity("http://localhost:8081/wallet-user/" + receiverId, User.class).getBody() ;
//        SendMailNotification sendMailNotification = SendMailNotification.builder()
//                .receiverMailId(receiver.getUserEmail())
//                .message()
//                .serviceType()
//                .subject()
//                .build() ;
//
//        Future<SendResult<String, String>> send = kafkaTemplate.send(TopicConstants.SEND_NOTIFICATION_TOPIC
//                , receivedData.getTransactionID() , objectMapper. writeValueAsString(initiateTransactionDTORequest)) ;
//    }

    public Map<String,User> findSenderAndReceiver(String id) {
        Transaction transaction = transactionRepository.findById(id).get() ;

        Long senderId = transaction.getSenderId();
        Long receiverId = transaction.getReceiverId();

        User sender = restTemplate.getForEntity("http://localhost:8081/wallet-user/" + senderId, User.class).getBody();
        User receiver = restTemplate.getForEntity("http://localhost:8081/wallet-user/" + receiverId, User.class).getBody() ;

        Map<String,User> senderAndReceiver = new HashMap<>(2);

        senderAndReceiver.put("sender : " , sender) ;
        senderAndReceiver.put("receiver : " , receiver) ;

        return senderAndReceiver ;

    }
}
