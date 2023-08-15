package com.mindhub.homebankig.dtos;

public class LoanDto {
    private Long id;
    private String name;
    private Double maxAmount;
    private Integer payments;

    public LoanDto(Long id, String name, Double maxAmount, Integer payments) {
        this.id = id;
        this.name = name;
        this.maxAmount = maxAmount;
        this.payments = payments;
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

    public Integer getPayments() {
        return payments;
    }
}
