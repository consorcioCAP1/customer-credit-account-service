package com.nttdata.bootcamp.customercreditaccountservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nttdata.bootcamp.customercreditaccountservice.documents.CreditAccount;
import com.nttdata.bootcamp.customercreditaccountservice.dto.CreditAccountDto;
import com.nttdata.bootcamp.customercreditaccountservice.dto.CreditdebtDto;
import com.nttdata.bootcamp.customercreditaccountservice.repository.CustomerCreditAccountRepository;
import com.nttdata.bootcamp.customercreditaccountservice.service.CustomerCreditAccountService;
import com.nttdata.bootcamp.customercreditaccountservice.utilities.ConvertJson;
import com.nttdata.bootcamp.customercreditaccountservice.utilities.CreditAccountBuilder;

import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class CustomerCreditAccountServiceImpl implements CustomerCreditAccountService{

	@Autowired
	CustomerCreditAccountRepository repository;

	@Value("${create-credit-debts.api.url}")
    private String createCreditDebtsApiUrl;
	
	public final String ACCOUNT_TYPE_CREDIT = "CREDIT";
	public final String ACCOUNT_TYPE_CARD_CREDIT = "CAR_CREDIT";

	public final String CLIENT_TYPE_PERSONAL = "PERSONAL";
	public final String CLIENT_TYPE_BUSINESS = "BUSINESS";
	
	@Override
	public Mono<CreditAccount> saveCreditAccount(CreditAccountDto creditAccount) {
		//un cliente personal puede tener solo una cuenta de credito
		if (creditAccount.getAccountType().equals(ACCOUNT_TYPE_CREDIT) && creditAccount.getTypeCustomer().equals(CLIENT_TYPE_PERSONAL)) {
			return repository.findByDniAndAccountType(creditAccount.getDni(), creditAccount.getAccountType())
					.hasElement()
		            .flatMap(hasElement -> {
		                if (hasElement) {
		                	log.info("El cliente : "+creditAccount.getDni() +" ya cuenta con un crédito.");
		                	return Mono.error(new RuntimeException("El Cliente ya cuenta con un crédito"));
		                } else {
		                	log.info("Registrando crédito bancario del cliente: "+ creditAccount.getDni());
		                	//contruyendo el document CustomerBankAccount
		                	CreditAccount creditAccountDocument = CreditAccountBuilder.buildCreditPersonal(creditAccount);
		                	try {
		                		//invocando a la creacion de las deudas de credito
								createDebtsAccount(creditAccountDocument);
							} catch (JsonProcessingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		            		return repository.save(creditAccountDocument);		            		
		                }
		            });			
		}
		else {
			//si es tarjeta de credito personal construimos su document
			log.info("registrando cuenta de credito, tipo: " + creditAccount.getAccountType());
			if(creditAccount.getAccountType().equals(ACCOUNT_TYPE_CARD_CREDIT) && creditAccount.getTypeCustomer().equals(CLIENT_TYPE_PERSONAL)) {
				CreditAccount creditAccountDocument = CreditAccountBuilder.buildCardCreditPersonal(creditAccount);
        		return repository.save(creditAccountDocument);
			}
			//si es tarjeta de credito empresarial construimos su document
			else if(creditAccount.getAccountType().equals(ACCOUNT_TYPE_CARD_CREDIT) && creditAccount.getTypeCustomer().equals(CLIENT_TYPE_BUSINESS)) {
				CreditAccount creditAccountDocument = CreditAccountBuilder.buildCardCreditBusiness(creditAccount);
        		return repository.save(creditAccountDocument);
			}
			//si es cuenta de credito empresarial construimos su document
			else {
				//invocamos a la creacion de deuda de credito
				CreditAccount creditAccountDocument = CreditAccountBuilder.buildCreditBusiness(creditAccount);
        		try {
            		//invocando a la creacion de las deudas de credito
					createDebtsAccount(creditAccountDocument);
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
				return repository.save(creditAccountDocument);
			}
		}		 
	}

	//metodo para el consumo de la api de creacion de deudas de credito
	public void createDebtsAccount(CreditAccount creditAccountDocument) throws JsonProcessingException{
		CreditdebtDto creditDebts = CreditdebtDto.builder()
				.bankAccountNumber(creditAccountDocument.getBankAccountNumber())
				.numberBankPaymentInstallments(creditAccountDocument.getPaymentBankFee())
				.paymentAmount(creditAccountDocument.getPaymentAmountBankFee())
				.paymentStartDate(creditAccountDocument.getPaymentStartDate())
				.build();
		String objectToJson = ConvertJson.toJson(creditDebts);
		WebClient webClient = WebClient.create();
		webClient.post()
	        .uri(createCreditDebtsApiUrl)
	        .contentType(MediaType.APPLICATION_JSON)
	        .body(BodyInserters.fromValue(objectToJson))
	        .retrieve()
	        .bodyToMono(String.class)
	        .subscribe(response -> {
	            // Preguntar como manejar una respuesta api
	            log.info("respuesta obtenida de invocar api: "+response);
        });        

	}
	

}
