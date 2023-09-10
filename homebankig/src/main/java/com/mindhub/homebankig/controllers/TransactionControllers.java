package com.mindhub.homebankig.controllers;

import com.mindhub.homebankig.dtos.TransactionDTO;
import com.mindhub.homebankig.models.Account;
import com.mindhub.homebankig.models.Client;
import com.mindhub.homebankig.models.Transaction;
import com.mindhub.homebankig.models.TransactionType;
import com.mindhub.homebankig.repositories.AccountRepository;
import com.mindhub.homebankig.repositories.CardRepository;
import com.mindhub.homebankig.repositories.ClientRepository;
import com.mindhub.homebankig.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class TransactionControllers {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CardRepository cardRepository;

    @RequestMapping("/transactions")
    public List<TransactionDTO> getTransactions(){
        List<Transaction> listTransaction = transactionRepository.findAll();
        List<TransactionDTO> listTransactionDTO =
                listTransaction
                        .stream()
                        .map(transaction -> new TransactionDTO(transaction))
                        .collect(Collectors.toList());

        return listTransactionDTO;
    }
    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> registerTransaction(Authentication authentication,
                                                              @RequestParam Double amount,
                                                              @RequestParam String description,
                                                              @RequestParam String fromAccountNumber,
                                                              @RequestParam String toAccountNumber){
        if (authentication != null){
            Client client = clientRepository.findByEmail(authentication.getName());

            Account sourceAccount = accountRepository.findByNumber(fromAccountNumber);
            Account targetAccount = accountRepository.findByNumber(toAccountNumber);

            if(Double.isNaN(amount)){
                return new ResponseEntity<>("The amount is empty", HttpStatus.FORBIDDEN);
            }
            if(amount <= 0.0){
                return new ResponseEntity<>("Some of the Amount is not valid",HttpStatus.FORBIDDEN);
            }
            if(description.isEmpty()){
                return new ResponseEntity<>("The description is empty.", HttpStatus.FORBIDDEN);
            }
            if(fromAccountNumber.isEmpty()){
                return new ResponseEntity<>("The destination account is empty", HttpStatus.FORBIDDEN);
            }
            if(toAccountNumber.isEmpty()){
                //cuenta o descripcion vacia
                return new ResponseEntity<>("The origin account  is empty", HttpStatus.FORBIDDEN);
            }
            if(toAccountNumber.equals(fromAccountNumber)){
                // Cuenta de origen igual a la de destino
                return new ResponseEntity<>("Source account is equal to destination account", HttpStatus.FORBIDDEN);
            }
            if(targetAccount == null){
                //La cuenta de origen no existe
                return new ResponseEntity<>("The origin account does not exist",HttpStatus.FORBIDDEN);
            }

            //verifica si la cuenta pertenece al cliente logueado
            Set<Account> setNumberOrigin = client.getAccounts();
            if(setNumberOrigin.contains(toAccountNumber)){
                return new ResponseEntity<>("Unauthenticated account",HttpStatus.FORBIDDEN);
            }
            //La cuenta destino no existe
            if (sourceAccount == null){
                return new ResponseEntity<>("The destiny account does not exist", HttpStatus.FORBIDDEN);
            }
            //La cuenta de origen no tiene el monto para la transaccion
            if(targetAccount.getBalance() < amount || (amount <= 0)){
                return new ResponseEntity<>("Insufficient balance", HttpStatus.FORBIDDEN);
            }

            Transaction devitTransaction = new Transaction(TransactionType.DEBIT, amount, sourceAccount.getNumber() + " " + description, LocalDateTime.now(), sourceAccount);
            Transaction creditTransaction = new Transaction(TransactionType.CREDIT, amount, targetAccount.getNumber()+" "+ description, LocalDateTime.now(),targetAccount);

            transactionRepository.save(devitTransaction);
            transactionRepository.save(creditTransaction);

            Double auxOrigin = sourceAccount.getBalance()-amount;
            Double auxDestiny = targetAccount.getBalance()+amount;

            targetAccount.setBalance(auxDestiny);
            sourceAccount.setBalance(auxOrigin);

            return new ResponseEntity<>("201 successful transaction",HttpStatus.CREATED);

        } return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}