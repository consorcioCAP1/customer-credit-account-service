package com.nttdata.bootcamp.customercreditaccountservice.utilities;

import com.nttdata.bootcamp.customercreditaccountservice.documents.CreditAccount;
import com.nttdata.bootcamp.customercreditaccountservice.dto.CreditAccountDto;

public class CreditAccountBuilder {

	public static CreditAccount buildCreditPersonal(CreditAccountDto dto) {
		return CreditAccount.builder()
				.dni(dto.getDni())
				.typeCustomer(dto.getTypeCustomer())
				.paymentBankFee(dto.getPaymentBankFee())
				.paymentStartDate(dto.getPaymentStartDate())
				.paymentAmountBankFee(dto.getPaymentAmountBankFee())
				.accountType(dto.getAccountType())
				.openingDate(dto.getOpeningDate())
				.bankAccountNumber(dto.getBankAccountNumber()).build();
	}

	public static CreditAccount buildCardCreditPersonal(CreditAccountDto dto) {
		return CreditAccount.builder()
				.dni(dto.getDni())
				.clientId(dto.getClientId())
				.typeCustomer(dto.getTypeCustomer())
				.accountType(dto.getAccountType())
				.accountBalance(dto.getAccountBalance())
				.openingDate(dto.getOpeningDate())
				.bankAccountNumber(dto.getBankAccountNumber()).build();
	}

	public static CreditAccount buildCardCreditBusiness(CreditAccountDto dto) {
		return CreditAccount.builder()
				.ruc(dto.getRuc())
				.businessName(dto.getBusinessName())
				.clientId(dto.getClientId())
				.typeCustomer(dto.getTypeCustomer())
				.accountType(dto.getAccountType())
				.accountBalance(dto.getAccountBalance())
				.openingDate(dto.getOpeningDate())
				.bankAccountNumber(dto.getBankAccountNumber()).build();
	}

	public static CreditAccount buildCreditBusiness(CreditAccountDto dto) {
		return  CreditAccount.builder()
				.ruc(dto.getRuc())
				.businessName(dto.getBusinessName())
				.clientId(dto.getClientId())
				.typeCustomer(dto.getTypeCustomer())
				.paymentBankFee(dto.getPaymentBankFee())
				.paymentStartDate(dto.getPaymentStartDate())
				.paymentAmountBankFee(dto.getPaymentAmountBankFee())
				.accountType(dto.getAccountType())
				.openingDate(dto.getOpeningDate())
				.bankAccountNumber(dto.getBankAccountNumber()).build();
	}
}
