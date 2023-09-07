package com.mindhub.homebankig.repositories;

import com.mindhub.homebankig.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDateTime;

@RepositoryRestResource
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}