package com.mindhub.homebankig.dtos;

import com.mindhub.homebankig.models.Loan;

public class LoanAplicationDTO {
    private String loanTypeId;
    private Double amount;
    private Integer payments;
    private String toAccountNumber;

    public LoanAplicationDTO() {
    }

    public LoanAplicationDTO(String loanTypeId, Double amount, Integer payments, String toAccountNumber) {
        this.loanTypeId = loanTypeId;
        this.amount = amount;
        this.payments = payments;
        this.toAccountNumber = toAccountNumber;
    }

    public String getLoanTypeId() {
        return loanTypeId;
    }

    public Double getAmount() {
        return amount;
    }

    public Integer getPayments() {
        return payments;
    }

    public String getToAccountNumber() {
        return toAccountNumber;
    }
}
