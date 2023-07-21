package com.nttdata.bootcamp.customercreditaccountservice.utilities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.nttdata.bootcamp.customercreditaccountservice.documents.CreditAccount;
import com.nttdata.bootcamp.customercreditaccountservice.dto.CreditAccountDto;

public class CreditAccountBuilder {

	private CreditAccountBuilder() {}
	public static CreditAccount buildCreditPersonal(CreditAccountDto dto) {
		LocalDateTime currentDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return CreditAccount.builder()
				.dni(dto.getDni())
				.typeCustomer(dto.getTypeCustomer())
				.paymentBankFee(dto.getPaymentBankFee())
				.paymentStartDate(dto.getPaymentStartDate())
				.paymentAmountBankFee(dto.getPaymentAmountBankFee())
				.accountType(dto.getAccountType())
				.openingDate(currentDate.format(formatter))
				.bankAccountNumber(dto.getBankAccountNumber()).build();
	}

	public static CreditAccount buildCardCreditPersonal(CreditAccountDto dto) {
		LocalDateTime currentDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return CreditAccount.builder()
				.dni(dto.getDni())
				.clientId(dto.getClientId())
				.typeCustomer(dto.getTypeCustomer())
				.accountType(dto.getAccountType())
				.accountBalance(dto.getAccountBalance())
				.openingDate(currentDate.format(formatter))
				.bankAccountNumber(dto.getBankAccountNumber()).build();
	}

	public static CreditAccount buildCardCreditBusiness(CreditAccountDto dto) {
		LocalDateTime currentDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return CreditAccount.builder()
				.ruc(dto.getRuc())
				.businessName(dto.getBusinessName())
				.clientId(dto.getClientId())
				.typeCustomer(dto.getTypeCustomer())
				.accountType(dto.getAccountType())
				.accountBalance(dto.getAccountBalance())
				.openingDate(currentDate.format(formatter))
				.bankAccountNumber(dto.getBankAccountNumber()).build();
	}

	public static CreditAccount buildCreditBusiness(CreditAccountDto dto) {
		LocalDateTime currentDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return  CreditAccount.builder()
				.ruc(dto.getRuc())
				.businessName(dto.getBusinessName())
				.clientId(dto.getClientId())
				.typeCustomer(dto.getTypeCustomer())
				.paymentBankFee(dto.getPaymentBankFee())
				.paymentStartDate(dto.getPaymentStartDate())
				.paymentAmountBankFee(dto.getPaymentAmountBankFee())
				.accountType(dto.getAccountType())
				.openingDate(currentDate.format(formatter))
				.bankAccountNumber(dto.getBankAccountNumber()).build();
	}
}
