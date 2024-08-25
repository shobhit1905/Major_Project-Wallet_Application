package com.majorproject.notification_service.templates;

import com.majorproject.notification_service.DTO.MailComponents;

public class MailsTemplates {

    public static MailComponents getWalletCreationbody(){
        return MailComponents.builder()
                .mailSubject("New Wallet Onboarding")
                .mailBody("Hi %s, \n " +
                          "Your Wallet account is Created. Now you have access to all the Features offered by the Wallet. \n" +
                          "You daily limit is Rs. 10000 and daily Transactions limit is 10. You can update this anytime you want.\n" +
                          "Thanks & Regards,\n" +
                          "Wallet Team")
                .build();
    }
}
