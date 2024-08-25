package com.majorproject.wallet_service.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.majorproject.jbdl_wallet_library.DTO.InitiateTransactionDTO;
import com.majorproject.jbdl_wallet_library.DTO.UserWalletCreationRequest;
import com.majorproject.jbdl_wallet_library.constants.TopicConstants;
import com.majorproject.wallet_service.service.WalletService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.Map;

@Slf4j
@Configuration
public class KafkaConfiguration {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WalletService walletService;

    @KafkaListener(topics = TopicConstants.USER_CREATION_TOPIC, groupId = "user_service.walletCreation")
    public void pollMessageForUserCreation(ConsumerRecord receivedMessage) throws JsonProcessingException {
        UserWalletCreationRequest receivedData = objectMapper.readValue(receivedMessage.value().toString(), UserWalletCreationRequest.class);
        log.info("Received Data : {}", receivedData.toString());
        walletService.createWalletForNewUser(receivedData);
    }

    @KafkaListener(topics = TopicConstants.INITIATE_TRANSACTION_TOPIC, groupId = "successful_transaction")
    public void pollMessageForBalanceUpdate(ConsumerRecord receivedMessage) throws JsonProcessingException {
        InitiateTransactionDTO receivedData = objectMapper.readValue(receivedMessage.value().toString(), InitiateTransactionDTO.class);
        log.info("Received Data : {}", receivedData);
        walletService.updateBalanceForUsers(receivedData);
    }
}


