package com.mindhub.homebankig.dtos;

import com.mindhub.homebankig.models.Account;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountDTO {
    private Long id;
    private String number;
    private LocalDateTime date;
    private Double balance;
    private Set<TransactionDTO> transactions;
    public AccountDTO(Account account){
        id = account.getId();
        number = account.getNumber();
        date = account.getDate();
        balance = account.getBalance();
        transactions = account
                .getTransactions()
                .stream()
                .map(element ->new TransactionDTO(element))
                .collect(Collectors.toSet());
    }

    public Long getId() {return id;}

    public String getNumber() {
        return number;
    }

    public LocalDateTime getCreationDate() {
        return date;
    }

    public Double getBalance() {
        return balance;
    }

    public Set<TransactionDTO> getTransactions() {
        return transactions;
    }
}