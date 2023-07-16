package com.nttdata.bootcamp.customercreditaccountservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nttdata.bootcamp.customercreditaccountservice.documents.CreditAccount;
import com.nttdata.bootcamp.customercreditaccountservice.dto.CreditAccountDto;
import com.nttdata.bootcamp.customercreditaccountservice.repository.CustomerCreditAccountRepository;
import com.nttdata.bootcamp.customercreditaccountservice.service.CustomerCreditAccountService;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class CustomerCreditAccountServiceImpl implements CustomerCreditAccountService{

	@Autowired
	CustomerCreditAccountRepository repository;
	
	@Override
	public Mono<CreditAccount> saveCreditAccount(CreditAccountDto creditAccount) {
		log.info("registrando cuenta de credito, tipo: " + creditAccount.getAccountType());
		CreditAccount customerBankAccountDocument = CreditAccount.builder()
				.dni(creditAccount.getDni())
				.ruc(creditAccount.getRuc())
				.businessName(creditAccount.getBusinessName())
				.clientId(creditAccount.getClientId())
				.typeCustomer(creditAccount.getTypeCustomer())
				.paymentBankFee(creditAccount.getPaymentBankFee())
				.paymentStartDate(creditAccount.getPaymentStartDate())
				.paymentAmountBankFee(creditAccount.getPaymentAmountBankFee())
				.accountType(creditAccount.getAccountType())
				.accountBalance(creditAccount.getAccountBalance())
				.openingDate(creditAccount.getOpeningDate())
				.bankAccountNumber(creditAccount.getBankAccountNumber()).build();
		   return repository.save(customerBankAccountDocument);

		 
	}
	


	
}
