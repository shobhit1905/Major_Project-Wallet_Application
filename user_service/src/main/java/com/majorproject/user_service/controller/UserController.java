package com.majorproject.user_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.majorproject.user_service.DTO.UpdateUserAddressDTO;
import com.majorproject.user_service.DTO.UpdateUserEmail;
import com.majorproject.user_service.DTO.UpdateUserMobileDTO;
import com.majorproject.user_service.DTO.UserResponseDTO;
import com.majorproject.user_service.model.User;
import com.majorproject.user_service.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wallet-user")
@Slf4j
@Validated
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping
    public ResponseEntity<UserResponseDTO> createNewUser(@RequestBody @Valid User user){
        try{
            UserResponseDTO u = userService.createNewUser(user) ;
            return new ResponseEntity<>(u , HttpStatus.CREATED) ;
        }
        catch(RuntimeException e){
            log.error("Not able to add new user , due to exception : " + e.getMessage()) ;
            return new ResponseEntity<>(null , HttpStatus.NOT_ACCEPTABLE) ;
        }
        catch (JsonProcessingException e) {
            log.error("Exception occurred : " + e.getMessage() + " cannot create new User") ;
            return new ResponseEntity<>(null , HttpStatus.NOT_ACCEPTABLE) ;
        }
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        try{
            List<User> users = userService.getAllUsers() ;
            return new ResponseEntity<>(users , HttpStatus.FOUND) ;
        }
        catch(RuntimeException e){
            log.error("Not able to fetch information for all users , Exception occurred : " + e.getMessage()) ;
            return new ResponseEntity<>(null , HttpStatus.NOT_FOUND) ;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id){
        try{
            User user = userService.getUserById(id);
            if(user != null)
                return new ResponseEntity<>(user, HttpStatus.FOUND);
            else {
                log.error("No user exist with id : " + id) ;
                return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
            }
        }
        catch(RuntimeException e){
            log.error("Not able to fetch information for user , Exception occurred : " + e.getMessage()) ;
            return new ResponseEntity<>(null , HttpStatus.NOT_FOUND) ;
        }
    }

    @PutMapping("/mobile")
    public ResponseEntity<String> updateUserMobile(@RequestBody UpdateUserMobileDTO user){
        try{
            Boolean updated = userService.updateUserMobile(user);
            if (updated) {
                log.info("Mobile Number for userId : " + user.getUserId() + " updated successfully");
                return new ResponseEntity<>(String.format("Mobile number for userId : %s updated successfully", user.getUserId()), HttpStatus.ACCEPTED);
            } else {
                log.error("User with id : " + user.getUserId() + " not found");
                return new ResponseEntity<>("User with id : " + user.getUserId() + " not found", HttpStatus.NOT_FOUND);
            }
        }
        catch(RuntimeException e){
            log.error("Not able to update mobile number , Exception occurred : " + e.getMessage()) ;
            return new ResponseEntity<>(null , HttpStatus.NOT_ACCEPTABLE) ;
        }
    }

    @PutMapping("/address")
    public ResponseEntity<String> updateUserAddress(@RequestBody UpdateUserAddressDTO user){
        try{
            Boolean updated = userService.updateUserAddress(user);
            if (updated) {
                log.info("Address for userId : " + user.getUserId() + " updated successfully");
                return new ResponseEntity<>(String.format("Address for userId : %s updated successfully", user.getUserId()), HttpStatus.ACCEPTED);
            } else {
                log.error("User with id : " + user.getUserId() + " not found");
                return new ResponseEntity<>("User with id : " + user.getUserId() + " not found", HttpStatus.NOT_FOUND);
            }
        }
        catch(RuntimeException e){
            log.error("Not able to update address , Exception occurred : " + e.getMessage()) ;
            return new ResponseEntity<>(null , HttpStatus.NOT_ACCEPTABLE) ;
        }
    }

    @PutMapping("/email")
    public ResponseEntity<String> updateUserEmail(@RequestBody UpdateUserEmail user){
        try{
            Boolean updated = userService.updateUserEmail(user) ;

            if(updated){
                log.info("Email for userId : " + user.getUserId() + " updated successfully");
                return new ResponseEntity<>("Email for userId : " + user.getUserId() + " updated successfully", HttpStatus.ACCEPTED);
            }
            else{
                log.error("User with id : " + user.getUserId() + " not found");
                return new ResponseEntity<>("User with id : " + user.getUserId() + " not found", HttpStatus.NOT_FOUND);
            }
        }
        catch(RuntimeException e){
            log.error("Not able to update email , Exception occurred : " + e.getMessage()) ;
            return new ResponseEntity<>(null , HttpStatus.NOT_ACCEPTABLE) ;
        }
    }

    @DeleteMapping("delete-user/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable("id") Long id){
        try{
            Boolean deleted = userService.deleteUserById(id) ;
            if(deleted){
                log.info("User with id : " + id + " deleted successfully");
                return new ResponseEntity<>(String.format("User with id : %s deleted successfully", id), HttpStatus.OK);
            }
            else{
                log.error("User with id : " + id + " not found");
                return new ResponseEntity<>("User with id : " + id + " not found", HttpStatus.NOT_FOUND);
            }
        }
        catch(RuntimeException e){
            log.error("Not able to delete user , Exception occurred : " + e.getMessage()) ;
            return new ResponseEntity<>(null , HttpStatus.NOT_ACCEPTABLE) ;
        }
    }

    @GetMapping("username/{userName}")
    public ResponseEntity<User> getUserByUserName(@PathVariable("userName") String userName){
        try{
            User u = userService.getUserByName(userName) ;
            if(u != null){
                log.info("Information retrieved successfully for user : " + userName);
                return new ResponseEntity<>(u , HttpStatus.FOUND);
            }
            else{
                log.error("No user found with name : " + userName);
                return new ResponseEntity<>(null , HttpStatus.NOT_FOUND);
            }
        }
        catch(RuntimeException e){
            log.error("Not able to retrieve information for user , Exception occurred : " + e.getMessage()) ;
            return new ResponseEntity<>(null , HttpStatus.INTERNAL_SERVER_ERROR) ;
        }
    }

    @GetMapping("/userExistence/{userId}")
    public ResponseEntity<Boolean> checkForUserExistence(@PathVariable("userId") Long userId){
        return new ResponseEntity<>(userService.checkForUserExistence(userId) , HttpStatus.OK) ;
    }

}
