package com.mindhub.homebankig.dtos;

import com.mindhub.homebankig.models.Loan;

public class LoanAplicationDTO {
    private long loanId;
    private Double amount;
    private Integer payments;
    private String toAccountNumber;

    public LoanAplicationDTO() {
    }

    public LoanAplicationDTO(long loanId, Double amount, Integer payments, String toAccountNumber) {
        this.loanId = loanId;
        this.amount = amount;
        this.payments = payments;
        this.toAccountNumber = toAccountNumber;
    }

    public long getLoanId() { return loanId; }

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
