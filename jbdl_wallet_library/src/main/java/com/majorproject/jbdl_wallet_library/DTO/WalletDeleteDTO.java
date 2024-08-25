package com.majorproject.jbdl_wallet_library.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@Builder
@ToString
public class WalletDeleteDTO {

    private Long userId ;

    private Long walletId ;
}
