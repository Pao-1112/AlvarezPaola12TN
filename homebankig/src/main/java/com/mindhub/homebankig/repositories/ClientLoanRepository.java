package com.mindhub.homebankig.repositories;

import com.mindhub.homebankig.models.ClientLoan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientLoanRepository extends JpaRepository<ClientLoan, Long> {
}