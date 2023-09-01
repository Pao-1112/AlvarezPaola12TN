package com.mindhub.homebankig.repositories;

import com.mindhub.homebankig.models.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository <Loan, Long>{
}