package com.majorproject.jbdl_wallet_library.DTO;

import com.majorproject.jbdl_wallet_library.enums.PaymentStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class SuccessfulTransactionDTO {

    private String transactionID;

    private PaymentStatus paymentStatus;
}
