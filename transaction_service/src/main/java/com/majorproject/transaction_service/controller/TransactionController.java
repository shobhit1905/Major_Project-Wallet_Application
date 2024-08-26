package com.majorproject.transaction_service.controller;

import com.majorproject.transaction_service.DTO.UserTransactionRequest;
import com.majorproject.transaction_service.DTO.UserTransactionResponse;
import com.majorproject.transaction_service.entity.Transaction;
import com.majorproject.transaction_service.service.TransactionService;
import com.majorproject.user_service.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/transactions")
@Slf4j
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<UserTransactionResponse> createTransactionRequest(@RequestBody UserTransactionRequest request){
        try{
            return new ResponseEntity<>(transactionService.createTransactionRequest(request), HttpStatus.ACCEPTED);
        }
        catch(Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/findUsers/{id}")
    public ResponseEntity<Map<String,User>> findSenderAndReceiver(@PathVariable("id") String id){
        return new ResponseEntity<>(transactionService.findSenderAndReceiver(id), HttpStatus.FOUND) ;
    }

    @GetMapping("/transactionDetails/{id}")
    public ResponseEntity<Transaction> getTransactionDetails(@PathVariable("id") String transactionId){
        try{
            Transaction transaction = transactionService.getTransactionDetailsUsingId(transactionId) ;
            if(transaction != null){
                log.info("Retrieved transaction information successfully");
                return new ResponseEntity<>(transaction, HttpStatus.FOUND);
            }
            else{
                log.error("No transaction found for the provided id , Invalid id");
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        }
        catch(RuntimeException exception){
            log.error("Not able to fetch transaction details , Exception occurred : " + exception.getMessage()) ;
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // SELECT * FROM wallet_transactions WHERE creation_date BETWEEN {date1} AND {date2} --> History
}
