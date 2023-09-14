package com.mindhub.homebankig;

import com.mindhub.homebankig.models.*;
import com.mindhub.homebankig.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class RepositoriesTest {
    @Autowired
    LoanRepository loanRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    CardRepository cardRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    TransactionRepository transactionRepository;

    //verifica que existan prestamos en la base de datos
    @Test
    public void existLoans() {
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans, is(not(empty())));
    }
    // Verifica que la lista de prestamos exista uno llamado personal
    @Test
    public void existPersonalLoan(){
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans, hasItem(hasProperty("name", is("Personal"))));

    }

    //Verifica que existan cuentas en la base de datos
    @Test
    public void existAccounts() {
        List<Account> accounts = accountRepository.findAll();
        assertThat(accounts, is(not(nullValue())));
    }
    @Test
    public void accountBalanceNotNullValue(){
        List<Account> accountsBalance = accountRepository.findAll();
        assertThat(accountsBalance, hasItem(hasProperty("balance",notNullValue())));

    }

    //Verifica que existan tarjetas en la base de datos
    @Test
    public void existCards() {
        List<Card> cards = cardRepository.findAll();
        assertThat(cards, hasItem(hasProperty("cardHolder")));
    }

    @Test
    public void CardsType(){
        List<Card> cardsTypes = cardRepository.findAll();
        assertThat(cardsTypes, hasItem(hasProperty("type", instanceOf(CardType.CREDIT.getDeclaringClass()))));
        assertThat(cardsTypes, hasItem(hasProperty("type", instanceOf(CardType.DEBIT.getDeclaringClass()))));

    }

    @Test
    public void existClients() {
        List<Client> clients = clientRepository.findAll();
        assertThat(clients, hasItem(hasProperty("firstName",is(not(toString())))));

    }
    //Que email contenga @
    @Test
    public void emailClientsNotNullValue(){
        List<Client> clientsEmail = clientRepository.findAll();
        assertThat(clientsEmail, hasItem(hasProperty("email", containsString("@"))));

    }

    @Test
    public void existTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions, (hasItem(hasProperty("type", instanceOf(TransactionType.DEBIT.getDeclaringClass())))));
        assertThat(transactions, (hasItem(hasProperty("type", instanceOf(TransactionType.CREDIT.getDeclaringClass())))));
    }
    @Test
    public void transactionsAmountsNotZero(){
        List<Transaction> AmountTransactions = transactionRepository.findAll();
        assertThat(AmountTransactions, hasItem(hasProperty("amount", is(not( 0)))));

    }
    @Test
    public void typeDateAccount(){
        List<Account> account = accountRepository.findAll();
        assertThat(account, hasItem(hasProperty("date", notNullValue())));
    }
@Test
    public void colorOfCards() {
        List<Card> cardColor = cardRepository.findAll();
        assertThat(cardColor, hasProperty("color",instanceOf(CardColor.GOLD.getDeclaringClass())));
        assertThat(cardColor, hasProperty("color",instanceOf(CardColor.SILVER.getDeclaringClass())));
        assertThat(cardColor, hasProperty("color",instanceOf(CardColor.TITANIUM.getDeclaringClass())));
    }
}
