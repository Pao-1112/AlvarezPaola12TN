package com.mindhub.homebankig.dtos;


public class LoanAplicationDTO {
    private Long loanId;
    private Double amount;
    private Integer payments;
    private String toAccountNumber;

    public LoanAplicationDTO() {
    }

    public LoanAplicationDTO(Long loanId, Double amount, Integer payments, String toAccountNumber) {
        this.loanId = loanId;
        this.amount = amount;
        this.payments = payments;
        this.toAccountNumber = toAccountNumber;
    }

    public Long getLoanId() {
        return loanId;
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
