package com.majorproject.transaction_service.DTO;

import com.majorproject.jbdl_wallet_library.enums.PaymentStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserTransactionResponse {

    private String transactionId ;

    private String message ;

    private LocalDateTime transactionInitiationTime ;

    private PaymentStatus paymentStatus ;
}
