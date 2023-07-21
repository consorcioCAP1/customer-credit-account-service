package com.nttdata.bootcamp.customercreditaccountservice.repository;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import com.nttdata.bootcamp.customercreditaccountservice.documents.CreditAccount;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CreditAccountRepository extends ReactiveMongoRepository<CreditAccount, String>{
	Mono<CreditAccount> findByDniAndAccountType(String dni, String accountType);

	@Query("{ 'bankAccountNumber' : ?0 }")
	Mono<CreditAccount> findAccountBalanceByBankAccountNumber(String bankAccountNumber);
	
	Mono<CreditAccount> findByBankAccountNumber(String bankAccountNumber);
	
	Flux<CreditAccount> findByRucAndAccountType(String ruc, String accountType);
		
}
