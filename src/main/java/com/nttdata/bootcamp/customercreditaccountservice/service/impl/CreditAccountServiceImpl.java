package com.nttdata.bootcamp.customercreditaccountservice.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nttdata.bootcamp.customercreditaccountservice.documents.CreditAccount;
import com.nttdata.bootcamp.customercreditaccountservice.dto.CreditAccountDto;
import com.nttdata.bootcamp.customercreditaccountservice.dto.CreditdebtDto;
import com.nttdata.bootcamp.customercreditaccountservice.repository.CreditAccountRepository;
import com.nttdata.bootcamp.customercreditaccountservice.service.CreditAccountService;
import com.nttdata.bootcamp.customercreditaccountservice.utilities.ConvertJson;
import com.nttdata.bootcamp.customercreditaccountservice.utilities.CreditAccountBuilder;
import com.nttdata.bootcamp.customercreditaccountservice.utilities.ExternalApiService;

import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class CreditAccountServiceImpl implements CreditAccountService{

	@Autowired
	CreditAccountRepository repository;

	@Value("${create-credit-debts.api.url}")
    private String createCreditDebtsApiUrl;
	
	public static final String ACCOUNT_TYPE_CREDIT = "CREDIT";
	public static final String ACCOUNT_TYPE_CARD_CREDIT = "CREDITCARD";

	public static final String CLIENT_TYPE_PERSONAL = "PERSONAL";
	public static final String CLIENT_TYPE_BUSINESS = "BUSINESS";

	@Autowired
	ExternalApiService externalApiService;

	@Override
	public Mono<CreditAccount> saveCreditAccount(CreditAccountDto creditAccount) {
		//un cliente personal puede tener solo una cuenta de credito
		if (creditAccount.getAccountType().equals(ACCOUNT_TYPE_CREDIT) 
				&& creditAccount.getTypeCustomer().equals(CLIENT_TYPE_PERSONAL)) {
	        return savePersonalCreditAccount(creditAccount);
	    } else {
			//si es tarjeta de credito personal construimos su document
	        if (creditAccount.getAccountType().equals(ACCOUNT_TYPE_CARD_CREDIT) 
	        		&& creditAccount.getTypeCustomer().equals(CLIENT_TYPE_PERSONAL)) {
	            return savePersonalCreditCardAccount(creditAccount);
	        } 
			//si es tarjeta de credito empresarial construimos su document
	        else if (creditAccount.getAccountType().equals(ACCOUNT_TYPE_CARD_CREDIT) 
	        			&& creditAccount.getTypeCustomer().equals(CLIENT_TYPE_BUSINESS)) {
	            return saveBusinessCreditCardAccount(creditAccount);
	        }
			//si es cuenta de credito empresarial construimos su document
	        else {
	            return saveBusinessCreditAccount(creditAccount);
	        }
	    }
	}
	//metodo para obtener el saldo disponible del numero de cuenta
	@Override
	public Mono<Double> getAccountBalanceByBankAccountNumber(String bankAccountNumber){
		return repository.findAccountBalanceByBankAccountNumber(bankAccountNumber)
	            .map(creditAccount -> creditAccount.getAccountBalance());
    }

	//metodo para actualizar le account balance en base al numero de cuenta
	@Override
	public Mono<CreditAccount> updateAccountBalance(String bankAccountNumber, Double accountBalance) {
	        return repository.findByBankAccountNumber(bankAccountNumber)
	                .flatMap(creditAccount -> {
	                	creditAccount.setAccountBalance(accountBalance);
	                    return repository.save(creditAccount);
	                });
	   }

	//metodo para el consumo de la api de creacion de deudas de credito
	public void createDebtsAccount(CreditAccount creditAccountDocument) {
		CreditdebtDto creditDebts = CreditdebtDto.builder()
				.bankAccountNumber(creditAccountDocument.getBankAccountNumber())
				.numberBankPaymentInstallments(creditAccountDocument.getPaymentBankFee())
				.paymentAmount(creditAccountDocument.getPaymentAmountBankFee())
				.paymentStartDate(creditAccountDocument.getPaymentStartDate())
				.build();
		String objectToJson;
		try {
			objectToJson = ConvertJson.toJson(creditDebts);
			WebClient webClient = WebClient.create();
			webClient.post()
		        .uri(createCreditDebtsApiUrl)
		        .contentType(MediaType.APPLICATION_JSON)
		        .body(BodyInserters.fromValue(objectToJson))
		        .retrieve()
		        .bodyToMono(String.class)
		        .subscribe();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public Mono<CreditAccount> findByDniAndTypeAccount(String dni, String typeAccount){
		return repository.findByNumberDocumentAndAccountType(dni, typeAccount).next();
	}

	@Override
	public Flux<CreditAccount> findByRucAndTypeAccount(String ruc, String typeAccount){
		return repository.findByNumberDocumentAndAccountType(ruc, typeAccount);
	}	
	
	private Mono<CreditAccount> savePersonalCreditAccount(CreditAccountDto creditAccount) {
	    return repository.findByNumberDocumentAndAccountType(creditAccount.getNumberDocument(),
	    				creditAccount.getAccountType())
	            .next()
	    		.hasElement()
	            .flatMap(hasElement -> {
	                if (hasElement) {
	                    log.info("El cliente : " + creditAccount.getNumberDocument() 
	                    		+ " ya cuenta con un crédito.");
	                    return Mono.error(new RuntimeException("El Cliente ya cuenta con un crédito"));
	                } else {
	                    log.info("Registrando crédito bancario del cliente: " + creditAccount.getNumberDocument());
	                    CreditAccount creditAccountDocument = CreditAccountBuilder
	                    		.buildCreditPersonal(creditAccount);
	                    createDebtsAccount(creditAccountDocument);
	                    return repository.save(creditAccountDocument);
	                }
	            });
	}
	private Mono<CreditAccount> savePersonalCreditCardAccount(CreditAccountDto creditAccount) {
	    CreditAccount creditAccountDocument = CreditAccountBuilder.buildCardCreditPersonal(creditAccount);
	    return repository.save(creditAccountDocument);
	}

	private Mono<CreditAccount> saveBusinessCreditCardAccount(CreditAccountDto creditAccount) {
	    CreditAccount creditAccountDocument = CreditAccountBuilder.buildCardCreditBusiness(creditAccount);
	    return repository.save(creditAccountDocument);
	}

	private Mono<CreditAccount> saveBusinessCreditAccount(CreditAccountDto creditAccount) {
	    CreditAccount creditAccountDocument = CreditAccountBuilder.buildCreditBusiness(creditAccount);
	    createDebtsAccount(creditAccountDocument);
	    return repository.save(creditAccountDocument);
	}

	@Override
	public Flux<CreditAccount> findByNumberDocument(String numberDocument){
		return repository.findByNumberDocument(numberDocument);
	}
	
	//metodo que retorna la validación si se posee deuda vencida 
	public Mono<Boolean> clientHasDebts(String numberDocument) {
        return repository.findByNumberDocument(numberDocument)
            .collectList()
            .flatMap(accounts -> {
                if (!accounts.isEmpty()) {
                    List<String> accountNumbers = accounts.stream()
                            .map(CreditAccount::getBankAccountNumber)
                            .collect(Collectors.toList());
                    return externalApiService.getHasDebts(accountNumbers);
                } else {
                    return Mono.just(false);
                }
            });
    }
}
