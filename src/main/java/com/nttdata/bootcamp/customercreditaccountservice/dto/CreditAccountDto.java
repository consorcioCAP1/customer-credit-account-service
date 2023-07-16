package com.nttdata.bootcamp.customercreditaccountservice.dto;

import lombok.Data;

@Data
public class CreditAccountDto {
	private String id;
	private String typeCustomer;
	private String clientId;
	private String name;
	private String dni;
	private String businessName;
	private String ruc;
	private int paymentBankFee;
	private String paymentStartDate;
	private Double paymentAmountBankFee;
	private Double accountBalance;
	private String bankAccountNumber;
	private String accountType;
	private String openingDate;
}
