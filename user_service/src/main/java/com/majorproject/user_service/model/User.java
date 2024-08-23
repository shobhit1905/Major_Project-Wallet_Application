package com.majorproject.user_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "wallet_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId ;

    @Column(nullable = false)
    @NotBlank(message = "User name should not be blank")
    private String userFullName ;

    @Column(nullable = false)
    private Integer userAge ;

    @Column(nullable = false)
    @NotBlank(message = "User email should not be blank")
    private String userEmail ;

    @Column(nullable = false , unique = true)
    @NotBlank(message = "User mobile should not be blank")
    private String userMobile ;

    @Column(nullable = false , unique = true)
    @NotBlank(message = "User pancard should not be blank")
    private String userPancard ;

    @Column(nullable = false)
    private String userAddress ;

    @CreationTimestamp
    private LocalDateTime userCreationTime ;

    @UpdateTimestamp
    private LocalDateTime userModifiedTime ;
}
