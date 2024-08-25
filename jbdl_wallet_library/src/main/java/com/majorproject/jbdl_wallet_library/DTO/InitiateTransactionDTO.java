package com.majorproject.jbdl_wallet_library.DTO;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class InitiateTransactionDTO {

    private Long walletID ;

    private Long senderID ;

    private Long receiverId ;

    private Double amount ;

    private String transactionId ;
}
