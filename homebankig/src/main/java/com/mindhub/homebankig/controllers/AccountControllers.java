  package com.mindhub.homebankig.controllers;

import com.mindhub.homebankig.dtos.AccountDTO;
import com.mindhub.homebankig.models.Account;
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
    @RequestMapping("/accounts")
    public List<AccountDTO> getAccounts(){

        List<Account> listAccount = accountRepository.findAll();
        List<AccountDTO> listAccountDTO =
                listAccount
                        .stream()
                        .map(account -> new AccountDTO(account))
                        .collect(toList());

        return listAccountDTO;
    }
    @RequestMapping("/accounts/{id}")
    public AccountDTO getAccounts(@PathVariable long id){
        return new AccountDTO(accountRepository.findById(id));
    }

    @RequestMapping("/clients/current/accounts")
    public List<AccountDTO> getCurrentAccounts(Authentication authentication){
        return clientRepository
                .findByEmail(authentication.getName())
                .getAccounts()
                .stream()
                .map(AccountDTO::new)
                .collect(toList());
    }

    public int getRandomNumber(int min, int max){
        return (int) ((Math.random() * (max - min) + min));
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Account> registerAccount(Authentication authentication){

        if (authentication != null){
            Client client = clientRepository.findByEmail(authentication.getName());
            Account account;

            if (client.getAccounts().size()>=3){
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);

            }else {
                int accountNumber = getRandomNumber(10000000, 99999999);
                String numAccount;

                do{
                    numAccount = Integer.toString(accountNumber);

                }while(accountRepository.existsByNumber(numAccount));

                account = new Account(("VIN-" + accountNumber), LocalDateTime.now(), 0d);
                client.addAccount(account);
                accountRepository.save(account);
                return new ResponseEntity<>(HttpStatus.CREATED);
            }
        }return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
