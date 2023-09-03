package com.mindhub.homebankig.repositories;

import com.mindhub.homebankig.models.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {
    boolean existsByNumber(String number);
    boolean existsByCvv(int cvv);
}