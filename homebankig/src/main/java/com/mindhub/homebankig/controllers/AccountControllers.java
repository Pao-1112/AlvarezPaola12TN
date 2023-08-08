package com.mindhub.homebankig.controllers;

import com.mindhub.homebankig.dtos.AccountDTO;
import com.mindhub.homebankig.models.Account;
import com.mindhub.homebankig.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
}
