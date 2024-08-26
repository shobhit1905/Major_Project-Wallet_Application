package com.majorproject.wallet_service.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateMoneyLimit {

    private Long userId ;

    private Integer newLimit ;
}
