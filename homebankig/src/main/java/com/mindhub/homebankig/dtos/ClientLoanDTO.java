package com.mindhub.homebankig.dtos;

import com.mindhub.homebankig.models.ClientLoan;

public class ClientLoanDTO {
    private Long id;
    private String name;
    private Double amount;
    private Integer payments;
    private Long loanTypeId;
    public ClientLoanDTO(ClientLoan clientLoan){
        id = clientLoan.getId();
        name = clientLoan.getLoan().getName();
        amount = clientLoan.getAmount();
        payments = clientLoan.getPayments();
        loanTypeId = clientLoan.getLoan().getId();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getAmount() {
        return amount;
    }

    public Integer getPayments() {
        return payments;
    }

    public Long getLoanTypeId() {
        return loanTypeId;
    }
}