package com.nttdata.bootcamp.customercreditaccountservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@EnableReactiveMongoRepositories
public class CustomerCreditAccountServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerCreditAccountServiceApplication.class, args);
	}

}
