package com.mindhub.homebankig;

import com.mindhub.homebankig.models.Account;
import com.mindhub.homebankig.models.Client;
import com.mindhub.homebankig.models.Transaction;
import com.mindhub.homebankig.models.TransactionType;
import com.mindhub.homebankig.repositories.AccountRepository;
import com.mindhub.homebankig.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.time.LocalDateTime;

@SpringBootApplication
public class HomebankigApplication {

	public static void main(String[] args) {

		SpringApplication.run(HomebankigApplication.class, args);
	}
	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository){
		return (args) ->{
			//Creo el cliente 1
			Client client1 = new Client();
			client1.setFirstName("Melba");
			client1.setLastName("Morel");
			client1.setEmail("melba@mindhub.com");

			//Creo el cliente 2
			Client client2 = new Client();
			client2.setFirstName("Juan");
			client2.setLastName("Perez");
			client2.setEmail("juan@mindhub.com");

			//Guardo ambos clientes en la base de datos
			clientRepository.save(client1);
			clientRepository.save(client2);

			//Creo tres cuentas
			Account account1 = new Account();
			Account account2 = new Account();
			Account account3 = new Account();
			//Agrego un numero de cuenta y dinero en las tres cuentas
			account1.setNumber("VIN001");
			account1.setBalance(5000.00);
			account1.setCreationDate(LocalDateTime.now());

			account2.setNumber("VIN002");
			account2.setBalance(7500.00);
			account2.setCreationDate(LocalDateTime.now().plusDays(1));

			account3.setNumber("VIN003");
			account3.setBalance(10000.00);
			account3.setCreationDate(LocalDateTime.of(2023, 8, 1, 22, 20, 32, 23));

			//Le asigno al cliente 1 y 2 las cuentas 1 y 2 con su contenido
			client1.addAccount(account1);
			client1.addAccount(account2);
			client2.addAccount(account3);
			//Guardo las cuentas en la base de datos
			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);

			//Creo tres transacciones, una para cada cuenta
			Transaction transaction1 = new Transaction();
			Transaction transaction2 = new Transaction();
			Transaction transaction3 = new Transaction();

			transaction1.setType(TransactionType.DEBIT);
			transaction1.setAmount(-300.0);
			transaction1.setDescription("Debit");
			transaction1.setDate(LocalDateTime.now());

			transaction2.setType(TransactionType.CREDIT);
			transaction2.setAmount(7700.50);
			transaction2.setDescription("Credit");
			transaction2.setDate(LocalDateTime.of(2023, 8, 7, 21, 15, 33));

			transaction3.setType(TransactionType.DEBIT);
			transaction3.setAmount(-80000.0);
			transaction3.setDescription("Debit");
			transaction3.setDate(LocalDateTime.now());
		};
	}
}
