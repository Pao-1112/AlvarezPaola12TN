package com.mindhub.homebankig.repositories;

import com.mindhub.homebankig.models.ClientLoan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface ClientLoanRepository extends JpaRepository<ClientLoan, Long> {
}