package com.majorproject.jbdl_wallet_library.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class UserWalletCreationRequest {

    private Long userId ;

    private String userName ;

    private String userEmailId ;
}
