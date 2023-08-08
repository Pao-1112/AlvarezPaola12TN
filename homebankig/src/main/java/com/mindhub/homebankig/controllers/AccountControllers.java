package com.mindhub.homebankig.controllers;

import com.mindhub.homebankig.dtos.AccountDTO;
import com.mindhub.homebankig.dtos.ClientDTO;
import com.mindhub.homebankig.models.Account;
import com.mindhub.homebankig.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountControllers {
    @Autowired
    private AccountRepository accountRepository;
    @RequestMapping("/accounts")
    public List<AccountDTO> getAccount(){

        List<Account> listAccount = accountRepository.findAll();
        List<AccountDTO> listAccountDTO =
                listAccount
                        .stream()
                        .map(account -> new AccountDTO(account))
                        .collect(Collectors.toList());

        return listAccountDTO;
    }
    @RequestMapping("/account2")
    public List<AccountDTO> getAccount2(){
        return accountRepository.findAll().stream().map(account -> new AccountDTO(account)).collect(Collectors.toList());

    }

    @RequestMapping("/accounts/{id}")//devuelve una sola cuenta
    public AccountDTO getAccount(@PathVariable long id){
        return new AccountDTO(accountRepository.findById(id).orElse(null));
    }
}
