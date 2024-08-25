package com.majorproject.notification_service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.majorproject.jbdl_wallet_library.DTO.SendMailNotification;
import com.majorproject.jbdl_wallet_library.constants.TopicConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

@Slf4j
@Configuration
public class KafkaConsumer {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SendMail sendMail ;

    @KafkaListener(topics = TopicConstants.SEND_NOTIFICATION_TOPIC , groupId = "send_notification")
    public void sendNotification(ConsumerRecord receivedMessage) throws JsonProcessingException {
        SendMailNotification sendNotification = objectMapper.readValue(receivedMessage.value().toString() , SendMailNotification.class);
        log.info("Received Notification from Service : {} with Message : {}", sendNotification.getServiceType().name(), sendNotification);
        sendMail.sendMail(sendNotification);
    }
}
