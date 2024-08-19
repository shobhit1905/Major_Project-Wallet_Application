package com.majorproject.user_service.DTO;

import com.majorproject.user_service.model.User;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
public class UserResponseDTO {

    private String message ;

    private User user ;
}
