package com.nttdata.bootcamp.customercreditaccountservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.nttdata.bootcamp.customercreditaccountservice.dto.CreditAccountDto;
import com.nttdata.bootcamp.customercreditaccountservice.service.CustomerCreditAccountService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/")
public class CustomerCreditAccountController {

	@Autowired
	private CustomerCreditAccountService customerBankAccountService;

	@PostMapping("/createCreditAccount")
    public Mono<ResponseEntity<Object>> createCreditAccount(@RequestBody CreditAccountDto creditAccountDto) {
		
		return customerBankAccountService.saveCreditAccount(creditAccountDto)
				 .flatMap(objResponse -> {
	                    ResponseEntity<Object> responseEntity = ResponseEntity.ok(objResponse);
	                    return Mono.just(responseEntity);
	                })
	                .onErrorResume(error -> {
	                    ResponseEntity<Object> responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.getMessage());
	                    return Mono.just(responseEntity);
	                });
	}
}
