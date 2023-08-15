package com.mindhub.homebankig.dtos;

import com.mindhub.homebankig.models.ClientLoan;

public class ClientLoanDTO {
    private Long id;
    private String name;
    private Double amount;
    private Integer payments;
    private Long loans_id;
    public ClientLoanDTO(ClientLoan clientLoan){
        id = clientLoan.getId();
        name = clientLoan.getLoan().getName();
        amount = clientLoan.getAmount();
        payments = clientLoan.getPayments();
        loans_id = clientLoan.getLoan().getId();
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

    public Long getLoans_id() {
        return loans_id;
    }
}
