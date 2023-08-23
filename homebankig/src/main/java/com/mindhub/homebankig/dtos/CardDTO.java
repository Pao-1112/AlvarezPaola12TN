package com.mindhub.homebankig.dtos;

import com.mindhub.homebankig.models.Card;
import com.mindhub.homebankig.models.CardColor;
import com.mindhub.homebankig.models.CardType;

import java.time.LocalDateTime;

public class CardDTO {
    private Long id;
    private String cardHolder;
    private CardType type;
    private CardColor color;
    private String number;
    private Integer cvv;
    private LocalDateTime fromDate;
    private LocalDateTime thruDate;

    public CardDTO(Card card) {
        id = card.getId();
        cardHolder = card.getClient().getFirstName()+" "+ card.getClient().getLastName();
        type = card.getType();
        color = card.getColor();
        number = card.getNumber();
        cvv = card.getCvv();
        fromDate = card.getFromDate();
        thruDate = card.getThruDate();
    }
    public Long getId() {
        return id;
    }
    public String getCardHolder() {
        return cardHolder;
    }
    public CardType getType() {
        return type;
    }
    public CardColor getColor() {
        return color;
    }
    public String getNumber() {
        return number;
    }
    public Integer getCvv() {
        return cvv;
    }
    public LocalDateTime getFromDate() {
        return fromDate;
    }
    public LocalDateTime getThruDate() {
        return thruDate;
    }
}