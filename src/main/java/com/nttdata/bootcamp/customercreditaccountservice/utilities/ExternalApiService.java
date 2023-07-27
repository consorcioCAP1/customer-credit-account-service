package com.nttdata.bootcamp.customercreditaccountservice.utilities;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class ExternalApiService {

	@Value("${has-credit-debts.api.url}")
    private String hasDebtsApiUrl;
	
	//metodo para obtener el saldo disponible de tarjetas credito
	public Mono<Boolean> getHasDebts(List<String> accountNumbers) {
		WebClient webClient = WebClient.create(); 
		return webClient.get()
		            .uri(hasDebtsApiUrl,accountNumbers)
		            .retrieve()
		            .bodyToMono(Boolean.class);
	}


}
