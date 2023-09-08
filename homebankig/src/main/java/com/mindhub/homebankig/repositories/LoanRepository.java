package com.mindhub.homebankig.repositories;

import com.mindhub.homebankig.models.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface LoanRepository extends JpaRepository <Loan, Long>{
    Loan findById(long id);

}