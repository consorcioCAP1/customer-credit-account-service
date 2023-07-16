package com.nttdata.bootcamp.customercreditaccountservice.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import com.nttdata.bootcamp.customercreditaccountservice.documents.CreditAccount;

public interface CustomerCreditAccountRepository extends ReactiveMongoRepository<CreditAccount, String>{

}
