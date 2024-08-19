package com.majorproject.user_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserAddressDTO {
    private Long userId ;

    private String newAddress ;
}
