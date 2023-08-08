package com.mindhub.homebankig;

import com.mindhub.homebankig.models.Client;
import com.mindhub.homebankig.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HomebankigApplication {

	public static void main(String[] args) {

		SpringApplication.run(HomebankigApplication.class, args);
	}
	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository){
		return (args) ->{
			Client client = new Client(" Morel","Melba","melba@mindhub.com");

			clientRepository.save(client);

		};
	}

}
