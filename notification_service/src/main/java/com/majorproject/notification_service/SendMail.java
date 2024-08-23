package com.majorproject.notification_service;

import com.majorproject.jbdl_wallet_library.DTO.SendMailNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class SendMail {

    @Autowired
    private JavaMailSender javaMailSender ;

    public void sendMail(SendMailNotification notification) throws MailException {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("shobhit132623@gmail.com");
        simpleMailMessage.setSubject(notification.getSubject());
        simpleMailMessage.setTo(notification.getReceiverMailId());
        simpleMailMessage.setText(notification.getMessage());
        javaMailSender.send(simpleMailMessage);
    }
}
