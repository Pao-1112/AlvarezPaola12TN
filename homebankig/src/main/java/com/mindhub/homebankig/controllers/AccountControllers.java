  package com.mindhub.homebankig.controllers;

import com.mindhub.homebankig.dtos.AccountDTO;
import com.mindhub.homebankig.models.Account;
import com.mindhub.homebankig.models.Card;
import com.mindhub.homebankig.models.Client;
import com.mindhub.homebankig.repositories.AccountRepository;
import com.mindhub.homebankig.repositories.ClientRepository;
import com.mindhub.homebankig.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static com.mindhub.homebankig.utils.AccountUtils.getRandomAccountNumber;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class AccountControllers {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @GetMapping("/accounts")
    public List<AccountDTO> getAccounts(){

        List<Account> listAccount = accountRepository.findAll();
        List<AccountDTO> listAccountDTO =
                listAccount
                        .stream()
                        .map(account -> new AccountDTO(account))
                        .collect(toList());

        return listAccountDTO;
    }
    @GetMapping("/accounts/{id}")
    public AccountDTO getAccounts(@PathVariable long id){
        return new AccountDTO(accountRepository.findById(id));
    }

    @GetMapping("/clients/current/accounts")
    public List<AccountDTO> getCurrentAccounts(Authentication authentication){
        return clientRepository
                .findByEmail(authentication.getName())
                .getAccounts()
                .stream()
                .map(AccountDTO::new)
                .collect(toList());
    }

   /* public int getRandomAccountNumber(int min, int max){
        return (int) ((Math.random() * (max - min) + min));
    }*/

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> registerAccount(Authentication authentication){

        if (authentication != null){
            Client client = clientRepository.findByEmail(authentication.getName());
            Set<Account> accounts = client.getAccounts();

            if (client.getAccounts().size()>=3){
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);

            }else {
                int accountNumber = getRandomAccountNumber(10000000, 99999999);
                String numAccount = Integer.toString(accountNumber);

                if(accounts.stream().filter(account -> account.getNumber().equals(numAccount)).collect(toList()).isEmpty()){

                    Account account = new Account(("VIN-" + accountNumber), LocalDateTime.now(), 0d);
                    client.addAccount(account);
                    accountRepository.save(account);
                    return new ResponseEntity<>(HttpStatus.CREATED);

                } return new ResponseEntity<>("Option selected invalid, you already have a card of this type.", HttpStatus.FORBIDDEN);
            }
        }return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
