package com.mindhub.homebankig.controllers;

import com.mindhub.homebankig.dtos.TransactionDTO;
import com.mindhub.homebankig.models.Transaction;
import com.mindhub.homebankig.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class TransactionControllers {
    @Autowired
    private TransactionRepository transactionRepository;
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
    @RequestMapping("/transactions/{id}")
    public TransactionDTO getTransactions(@PathVariable long id){
        return new TransactionDTO(transactionRepository.findById(id).orElse(null));
    }

}
