package com.majorproject.transaction_service.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.majorproject.jbdl_wallet_library.DTO.SuccessfulTransactionDTO;
import com.majorproject.jbdl_wallet_library.DTO.UserWalletCreationRequest;
import com.majorproject.jbdl_wallet_library.constants.TopicConstants;
import com.majorproject.transaction_service.service.TransactionService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

@Configuration
public class TransactionConsumer {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TransactionService transactionService;

    @KafkaListener(topics = TopicConstants.SUCCESSFUL_TRANSACTION_TOPIC , groupId = "successful_transaction")
    public void pollSuccessfulOrFailedTransaction(ConsumerRecord receivedMessage) throws JsonProcessingException {
       SuccessfulTransactionDTO  receivedData = objectMapper.readValue(receivedMessage.value().toString(), SuccessfulTransactionDTO.class) ;
        transactionService.updateTransactionStatus(receivedData) ;
//        transactionService.sendUserNotification(receivedData) ;
    }
}
