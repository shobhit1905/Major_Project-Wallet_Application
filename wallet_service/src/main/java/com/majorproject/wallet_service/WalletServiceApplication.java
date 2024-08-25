package com.majorproject.wallet_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class WalletServiceApplication {

    @Bean
    public RestTemplate resttemplate(){
        return new RestTemplate() ;
    }

    public static void main(String[] args) {
        SpringApplication.run(WalletServiceApplication.class, args);
    }

}
