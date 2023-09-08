package com.mindhub.homebankig.dtos;

import com.mindhub.homebankig.models.Loan;

import java.util.Set;

public class LoanDTO {
    private Long id;
    private String name;
    private Double maxAmount;
    private Set<Integer> payments;

    public LoanDTO(Loan loan) {
        this.name = loan.getName();
        this.maxAmount = loan.getMaxAmount();
        this.payments = loan.getPayments();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getMaxAmount() {
        return maxAmount;
    }

    public Set<Integer> getPayments() {
        return payments;
    }
}
