package com.majorproject.wallet_service.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private WalletService walletService ;

    @KafkaListener(topics = TopicConstants.USER_CREATION_TOPIC , groupId = "user_service.walletCreation")
    public void pollMessageForUserCreation(ConsumerRecord receivedMessage) throws JsonProcessingException {
        Map<String,String> receivedData = objectMapper.readValue(receivedMessage.value().toString() , Map.class) ;
        log.info("Received Data : {}" , receivedData.get("userId"));
        walletService.createWalletForNewUser(receivedData) ;
    }
}
