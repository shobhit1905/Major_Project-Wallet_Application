package com.majorproject.transaction_service.DTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserTransactionRequest {

    private Long walletId ;

    private Long senderId ;

    private Long receiverId ;

    @DecimalMin("1.0")
    private Double amount ;
}
