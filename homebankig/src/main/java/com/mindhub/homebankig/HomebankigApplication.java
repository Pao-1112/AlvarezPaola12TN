package com.mindhub.homebankig;

import com.mindhub.homebankig.models.*;
import com.mindhub.homebankig.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Set;

@SpringBootApplication
public class HomebankigApplication {

	public static void main(String[] args) {

		SpringApplication.run(HomebankigApplication.class, args);
	}
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository,
									  AccountRepository accountRepository,
									  TransactionRepository transactionRepository,
									  LoanRepository loanRepository,
									  ClientLoanRepository clientLoanRepository,
									  CardRepository cardRepository){
		return (args) ->{
			//Client
			Client client1 = new Client();
			client1.setFirstName("Melba");
			client1.setLastName("Morel");
			client1.setEmail("melba@mindhub.com");
			client1.setPassword(passwordEncoder.encode("123456"));

			Client client2 = new Client();
			client2.setFirstName("Juan");
			client2.setLastName("Perez");
			client2.setEmail("juan@mindhub.com");
			client2.setPassword(passwordEncoder.encode("123456"));

			Client admin = new Client();
			admin.setFirstName("admin");
			admin.setLastName("admin");
			admin.setEmail("admin@mindhub.com");
			admin.setPassword(passwordEncoder.encode("123456"));

			clientRepository.save(client1);
			clientRepository.save(client2);
			clientRepository.save(admin);

			//Account
			Account account1 = new Account();
			Account account2 = new Account();
			Account account3 = new Account();

			account1.setNumber("VIN001");
			account1.setBalance(5000.00);
			account1.setCreationDate(LocalDateTime.now());

			account2.setNumber("VIN002");
			account2.setBalance(7500.00);
			account2.setCreationDate(LocalDateTime.now().plusDays(1));

			account3.setNumber("VIN003");
			account3.setBalance(10000.00);
			account3.setCreationDate(LocalDateTime.of(2023, 8, 1, 22, 20, 32, 23));

			//Accounts in Clients
			client1.addAccount(account1);
			client1.addAccount(account2);
			client2.addAccount(account3);

			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);

			//Transactions
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

			//Transactions in Accounts
			account1.addTransaction(transaction1);
			account2.addTransaction(transaction2);
			account3.addTransaction(transaction3);

			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);

			//Loan
			Loan loan1 = new Loan();
			loan1.setName("Hipotecario");
			loan1.setMaxAmount(500000.00);
			loan1.setPayments(Set.of(12, 24, 36, 48, 60));

			Loan loan2 = new Loan();
			loan2.setName("Personal");
			loan2.setMaxAmount(100000.00);
			loan2.setPayments(Set.of(6, 12, 24));

			Loan loan3 = new Loan();
			loan3.setName("Automotriz");
			loan3.setMaxAmount(300000.00);
			loan3.setPayments(Set.of(6, 12, 24,36));

			loanRepository.save(loan1);
			loanRepository.save(loan2);
			loanRepository.save(loan3);

			//ClientLoan
			ClientLoan clientLoan1 = new ClientLoan();
			ClientLoan clientLoan2 = new ClientLoan();
			ClientLoan clientLoan3 = new ClientLoan();
			ClientLoan clientLoan4 = new ClientLoan();

			clientLoan3.setAmount(400000.00);
			clientLoan3.setPayments(60);
			clientLoan3.setClient(client1);
			clientLoan3.setLoan(loan1);

			clientLoan4.setAmount(50000.00);
			clientLoan4.setPayments(12);
			clientLoan4.setClient(client1);
			clientLoan4.setLoan(loan2);

			clientLoan1.setAmount(100000.00);
			clientLoan1.setPayments(24);
			clientLoan1.setClient(client2);
			clientLoan1.setLoan(loan2);

			clientLoan2.setAmount(200000.00);
			clientLoan2.setPayments(36);
			clientLoan2.setClient(client2);
			clientLoan2.setLoan(loan3);

			clientLoanRepository.save(clientLoan1);
			clientLoanRepository.save(clientLoan2);
			clientLoanRepository.save(clientLoan3);
			clientLoanRepository.save(clientLoan4);

			//Card
			Card card1 = new Card();
			Card card2 = new Card();
			Card card3 = new Card();
			Card card4 = new Card();

			card1.setType(CardType.DEBIT);
			card1.setColor(CardColor.GOLD);
			card1.setNumber("4444 5445 3331 5578");
			card1.setCvv(153);
			card1.setFromDate(LocalDateTime.now());
			card1.setThruDate(LocalDateTime.now().plusYears(5));

			card2.setCardHolder(client1.getFirstName()+" "+client1.getLastName());
			card2.setType(CardType.CREDIT);
			card2.setColor(CardColor.TITANIUM);
			card2.setNumber("3312 5548 7775 1224");
			card2.setCvv(236);
			card2.setFromDate(LocalDateTime.now());
			card2.setThruDate(LocalDateTime.now().plusYears(5));

			card3.setCardHolder(client2.getFirstName()+" "+ client2.getLastName());
			card3.setType(CardType.DEBIT);
			card3.setColor(CardColor.GOLD);
			card3.setNumber("3214 6584 6574 9987");
			card3.setCvv(155);
			card3.setFromDate(LocalDateTime.now());
			card3.setThruDate(LocalDateTime.now().plusYears(5));

			card4.setCardHolder(client2.getFirstName()+" "+client2.getLastName());
			card4.setType(CardType.CREDIT);
			card4.setColor(CardColor.SILVER);
			card4.setNumber("7778 2224 7584 3321");
			card4.setCvv(232);
			card4.setFromDate(LocalDateTime.now());
			card4.setThruDate(LocalDateTime.now().plusYears(5));

			//Card in Client
			client1.addCard(card1);
			client1.addCard(card2);
			client2.addCard(card3);
			client2.addCard(card4);

			cardRepository.save(card1);
			cardRepository.save(card2);
			cardRepository.save(card3);
			cardRepository.save(card4);

		};
	}
}