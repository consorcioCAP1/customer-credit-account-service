package com.nttdata.bootcamp.customercreditaccountservice.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Document(collection = "customerCreditAccount")
@AllArgsConstructor
@Data
@Builder
public class CreditAccount {

	@Id
	private String id;
	private String typeCustomer;
	private String clientId;
	private String clientName;
	private String numberDocument;
	private Integer paymentBankFee;
	private String paymentStartDate;
	private Double paymentAmountBankFee;
	private Double accountBalance;
	private String bankAccountNumber;
	private String accountType;
	private String openingDate;
}
