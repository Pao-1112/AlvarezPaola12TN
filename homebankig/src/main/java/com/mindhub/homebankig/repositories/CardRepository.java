package com.mindhub.homebankig.repositories;

import com.mindhub.homebankig.models.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface CardRepository extends JpaRepository<Card, Long> {
    boolean existsByNumber(String number);
    boolean existsByCvv(int cvv);
}