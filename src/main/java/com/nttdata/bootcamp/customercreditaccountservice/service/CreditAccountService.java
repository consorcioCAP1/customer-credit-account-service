package com.nttdata.bootcamp.customercreditaccountservice.service;

import com.nttdata.bootcamp.customercreditaccountservice.documents.CreditAccount;
import com.nttdata.bootcamp.customercreditaccountservice.dto.CreditAccountDto;

import reactor.core.publisher.Mono;

public interface CreditAccountService {

	public Mono<CreditAccount> saveCreditAccount(CreditAccountDto creditAccount);
	public Mono<Double> getAccountBalanceByBankAccountNumber(String bankAccountNumber);
	public Mono<CreditAccount> updateAccountBalance(String bankAccountNumber, Double accountBalance);
}
