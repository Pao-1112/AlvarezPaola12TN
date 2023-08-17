package com.mindhub.homebankig.repositories;

import com.mindhub.homebankig.models.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {
}
