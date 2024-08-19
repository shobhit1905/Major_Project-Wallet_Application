package com.majorproject.jbdl_wallet_library.DTO;

import com.majorproject.jbdl_wallet_library.enums.ServiceType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class SendMailNotification {

    private String receiverMailId ;

    private String message ;

    private String subject ;

    private ServiceType serviceType ;
}
