package com.majorproject.user_service.DTO;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserEmail {

    private Long userId ;

    private String newEmail ;
}
