package com.majorproject.wallet_service.controller;

import com.majorproject.jbdl_wallet_library.DTO.WalletDeleteDTO;
import com.majorproject.wallet_service.entity.Wallet;
import com.majorproject.wallet_service.service.WalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/wallet-wallet")
@Slf4j
public class WalletController {

    @Autowired
    private WalletService walletService;

    @GetMapping("/user/{id}")
    public ResponseEntity<String> getWalletDetailsforUser(@PathVariable("id") Long userId){
        try{
            String details = walletService.getWalletDetailsForUser(userId);
            if (details == null) {
                log.error("No user exists with userId : " + userId);
                return new ResponseEntity<>("No user exists with userId : " + userId, HttpStatus.NOT_FOUND);
            } else {
                log.info(details);
                return new ResponseEntity<>(details, HttpStatus.FOUND);
            }
        }
        catch(RuntimeException e){
            log.error("Not able to retrieve wallet information for user , Exception occurred : " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<Wallet>> getAllWallets(){
        try{
            List<Wallet> wallets = walletService.getAllWalletDetails() ;
            if(wallets == null){
                log.error("No wallets exists") ;
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            else{
                log.info("details of all wallets retrieved successfully") ;
                return new ResponseEntity<>(wallets, HttpStatus.FOUND);
            }
        }
        catch(RuntimeException e){
            log.error("Not able to retrieve details for all wallets , Exception occurred : " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{walletId}")
    public ResponseEntity<Wallet> getWalletDetailsWithId(@PathVariable("walletId") Long walletId){
        try{
            Wallet w = walletService.getWalletDetailsWithId(walletId) ;
            if(w == null){
                log.error("No wallet exists with walletId : " + walletId);
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            else{
                log.info(w.toString());
                return new ResponseEntity<>(w, HttpStatus.FOUND);
            }
        }
        catch(RuntimeException e){
            log.error("Not able to retrieve wallet information for user , Exception occurred : " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @GetMapping("userWallet/{userName}")
//    public ResponseEntity<Wallet> getWalletDetailsUsingUserName(@PathVariable("userName") String userName){
//        try{
//            Wallet w = walletService.getWalletUsingUserName(userName);
//            if (w == null) {
//                log.error("No wallet exists for user : " + userName);
//                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//            } else {
//                log.info(w.toString());
//                return new ResponseEntity<>(w, HttpStatus.FOUND);
//            }
//        }
//        catch(RuntimeException e){
//            log.error("Not able to retrieve wallet for user : " + userName + " , Exception occurred : " + e.getMessage());
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @GetMapping("/balance/fetchBalance/{userId}")
    public ResponseEntity<Double> getBalance(@PathVariable("userId") Long userId){
        try{
            Double balance = walletService.getUserBalance(userId);
            if (balance == null) {
                log.error("No user exist for the id : " + userId);
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            } else {
                log.info(balance.toString());
                return new ResponseEntity<>(balance, HttpStatus.FOUND);
            }
        }
        catch(Exception e){
            log.error("Not able to retrieve balance for user , Exception occurred : " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("deleteWallet/{walletId}")
    public ResponseEntity<Boolean> deleteWalletById(@PathVariable("walletId") Long walletId){
        try{
            Boolean deleted = walletService.deleteWalletById(walletId) ;
            if(deleted){
                log.info("Wallet deleted successfully for wallet id : " + walletId);
                return new ResponseEntity<>(true, HttpStatus.OK);
            }
            else{
                log.error("No wallet exist for id : " + walletId);
                return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
            }
        }
        catch(RuntimeException e){
            log.error("Not able to retrieve wallet information for delete operation , Exception occurred : " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("getWallet/user/{userId}")
    public ResponseEntity<WalletDeleteDTO> getWalletDeleteDTO(@PathVariable("userId") Long userId){
        try{
            WalletDeleteDTO dto = walletService.getWalletDeleteDTO(userId) ;
            if(dto == null){
                log.error("No user exists with userId : " + userId);
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            else{
                log.info("Retrieved wallet information");
                 return new ResponseEntity<>(dto, HttpStatus.FOUND);
            }
        }
        catch(RuntimeException e){
            log.error("Not able to retrieve wallet information for delete operation , Exception occurred : " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
