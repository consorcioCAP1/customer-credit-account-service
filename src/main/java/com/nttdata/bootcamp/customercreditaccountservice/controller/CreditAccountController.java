package com.nttdata.bootcamp.customercreditaccountservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.nttdata.bootcamp.customercreditaccountservice.documents.CreditAccount;
import com.nttdata.bootcamp.customercreditaccountservice.dto.CreditAccountDto;
import com.nttdata.bootcamp.customercreditaccountservice.service.CreditAccountService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/")
public class CreditAccountController {

	@Autowired
	private CreditAccountService creditAccountService;

	@PostMapping("/createCreditAccount")
    public Mono<ResponseEntity<Object>> createCreditAccount(@RequestBody CreditAccountDto creditAccountDto) {
		
		return creditAccountService.saveCreditAccount(creditAccountDto)
				 .flatMap(objResponse -> {
	                    ResponseEntity<Object> responseEntity = ResponseEntity.ok(objResponse);
	                    return Mono.just(responseEntity);
	                })
	                .onErrorResume(error -> {
	                    ResponseEntity<Object> responseEntity = ResponseEntity
	                    		.status(HttpStatus.BAD_REQUEST).body(error.getMessage());
	                    return Mono.just(responseEntity);
	                });
	}

    @GetMapping("/getAccountBalance/{bankAccountNumber}")
    public Mono<Double> getAccountBalance(@PathVariable String bankAccountNumber) {
        return creditAccountService.getAccountBalanceByBankAccountNumber(bankAccountNumber);
    }

    @PutMapping("/updateAccountBalance/{bankAccountNumber}")
    public Mono<ResponseEntity<CreditAccount>> updateAccountBalance(@PathVariable String bankAccountNumber,
                                                                           @RequestParam Double accountBalance) {
        return creditAccountService.updateAccountBalance(bankAccountNumber, accountBalance)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    //metodo para obtener cantidad de cuentas en base al dni y tipo cuenta
    @GetMapping("/findByDniAndTypeAccount/{dni}/{type}")
    public Mono<CreditAccount> findByDniAndTypeAccount(@PathVariable String dni, @PathVariable String type) {
        return creditAccountService.findByDniAndTypeAccount(dni, type);
    }
    
    //metodo para obtener cantidad de cuentas en base al dni y tipo cuenta
    @GetMapping("/findByRucAndTypeAccount/{ruc}/{type}")
    public Flux<CreditAccount> findByRucAndTypeAccount(@PathVariable String ruc, @PathVariable String type) {
        return creditAccountService.findByRucAndTypeAccount(ruc, type);
    }

    //metodo para obtener cuentas de credito en base al numero de documento
    @GetMapping("/findByNumberDocument/{numberDocument}")
    public Flux<CreditAccount> findByNumberDocument(@PathVariable String numberDocument) {
        return creditAccountService.findByNumberDocument(numberDocument);
    }

}
