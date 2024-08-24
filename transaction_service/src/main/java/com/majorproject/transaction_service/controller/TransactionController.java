package com.majorproject.transaction_service.controller;

import com.majorproject.transaction_service.UserTransactionRequest;
import org.springframework.boot.autoconfigure.pulsar.PulsarProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    public ResponseEntity<> createTransactionRequest(@RequestBody UserTransactionRequest request) {}
}
