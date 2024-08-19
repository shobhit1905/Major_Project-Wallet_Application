package com.majorproject.notification_service.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfiguration {

    @Bean
    public JavaMailSender javaMailSender(){
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost("smtp.rediffmail.com");
        javaMailSender.setPort(587);
        javaMailSender.setUsername("rickchaudhurizoom@rediffmail.com");
        javaMailSender.setPassword("Kolkata@2003");

        Properties properties = javaMailSender.getJavaMailProperties();
        properties.put("mail.smtp.starttls.enable", true);
        properties.put("mail.debug" , true) ; // for debugging purposes

        return javaMailSender ;
    }
}
