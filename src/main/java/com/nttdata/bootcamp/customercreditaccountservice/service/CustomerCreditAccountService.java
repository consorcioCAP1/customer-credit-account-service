package com.nttdata.bootcamp.customercreditaccountservice.service;

import com.nttdata.bootcamp.customercreditaccountservice.documents.CreditAccount;
import com.nttdata.bootcamp.customercreditaccountservice.dto.CreditAccountDto;

import reactor.core.publisher.Mono;

public interface CustomerCreditAccountService {

	public Mono<CreditAccount> saveCreditAccount(CreditAccountDto creditAccount);

}
