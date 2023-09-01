package com.mindhub.homebankig.controllers;

import com.mindhub.homebankig.dtos.AccountDTO;
import com.mindhub.homebankig.models.Account;
import com.mindhub.homebankig.models.Client;
import com.mindhub.homebankig.repositories.AccountRepository;
import com.mindhub.homebankig.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountControllers {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;
    @RequestMapping("/accounts")
    public List<AccountDTO> getAccounts(){

        List<Account> listAccount = accountRepository.findAll();
        List<AccountDTO> listAccountDTO =
                listAccount
                        .stream()
                        .map(account -> new AccountDTO(account))
                        .collect(Collectors.toList());

        return listAccountDTO;
    }
    @RequestMapping("/accounts/{id}")
    public AccountDTO getAccounts(@PathVariable long id){
        return new AccountDTO(accountRepository.findById(id).orElse(null));
    }
    public int getRandomNumber(int min, int max){
        return (int) ((Math.random() * (max - min) + min));
    }
    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Account> registerAccount(Authentication authentication, Client client){
        client = clientRepository.findByEmail(authentication.getName());
        if (authentication != null){
            Account account;
            int accountNumber = getRandomNumber(10000000, 99999999);

            if (client.getAccounts().size()>=3){
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }else {
                account = new Account(("VIN-" + accountNumber), LocalDateTime.now(), 0d);
                client.addAccount(account);
                accountRepository.save(account);
                return new ResponseEntity<>(HttpStatus.CREATED);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}