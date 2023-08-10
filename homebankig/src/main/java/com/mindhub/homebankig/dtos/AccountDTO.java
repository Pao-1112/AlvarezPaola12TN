package com.mindhub.homebankig.dtos;

import com.mindhub.homebankig.models.Account;
import java.time.LocalDateTime;

public class AccountDTO {
    private Long id;
    private String number;
    private LocalDateTime date;
    private Double balance;
    public AccountDTO(Account account){
        id = account.getId();
        number = account.getNumber();
        date = account.getCreationDate();
        balance = account.getBalance();
    }

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Double getBalance() {
        return balance;
    }

}
