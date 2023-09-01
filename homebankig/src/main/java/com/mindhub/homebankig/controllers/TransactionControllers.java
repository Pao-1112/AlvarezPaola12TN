package com.mindhub.homebankig.controllers;

import com.mindhub.homebankig.dtos.TransactionDTO;
import com.mindhub.homebankig.models.Account;
import com.mindhub.homebankig.models.Client;
import com.mindhub.homebankig.models.Transaction;
import com.mindhub.homebankig.repositories.AccountRepository;
import com.mindhub.homebankig.repositories.ClientRepository;
import com.mindhub.homebankig.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Set;

import static com.mindhub.homebankig.models.TransactionType.CREDIT;
import static  com.mindhub.homebankig.models.TransactionType.DEBIT;
@RestController
@RequestMapping("/api")
public class TransactionControllers {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<TransactionDTO> registerTransaction(Authentication authentication,
                                                              @RequestParam Double amount,
                                                              @RequestParam String description,
                                                              @RequestParam String numberDestiny,
                                                              @RequestParam String numberOrigin){
        Client client = clientRepository.findByEmail(authentication.getName());
        Account accountDestiny = accountRepository.findByNumber(numberDestiny);
        Account accountOrigin = accountRepository.findByNumber(numberOrigin);

        if(amount == null || description == null || numberDestiny == null || numberOrigin == null){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if(numberOrigin.equals(numberDestiny)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if(accountOrigin == null){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Set<Account> setNumberOrigin = client.getAccounts();
        if(setNumberOrigin.contains(numberOrigin)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if (accountDestiny == null){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if(accountOrigin.getBalance() < amount || amount <=0){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Transaction transactionOrigin = new Transaction(CREDIT, amount, accountOrigin.getNumber()+""+ description, LocalDateTime.now(),accountOrigin);
        Transaction transactionDestiny = new Transaction(DEBIT, amount, accountDestiny.getNumber() + "" + description, LocalDateTime.now(), accountDestiny);
        transactionRepository.save(transactionOrigin);
        transactionRepository.save(transactionDestiny);

        Double auxOrigin = accountOrigin.getBalance()-amount;
        Double auxDestiny = accountDestiny.getBalance()+amount;

        accountDestiny.setBalance(auxDestiny);
        accountOrigin.setBalance(auxOrigin);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}