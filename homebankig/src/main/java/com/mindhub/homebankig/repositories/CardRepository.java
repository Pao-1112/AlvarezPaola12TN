package com.mindhub.homebankig.repositories;

import com.mindhub.homebankig.models.Card;
import com.mindhub.homebankig.models.CardColor;
import com.mindhub.homebankig.models.CardType;
import com.mindhub.homebankig.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {
    Card findByNumber(String number);
}