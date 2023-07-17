package com.nttdata.bootcamp.customercreditaccountservice.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.nttdata.bootcamp.customercreditaccountservice.documents.CreditAccount;

import reactor.core.publisher.Mono;

public interface CustomerCreditAccountRepository extends ReactiveMongoRepository<CreditAccount, String>{
	Mono<CreditAccount> findByDniAndAccountType(String dni, String accountType);
}
