package com.mindhub.homebankig.repositories;

import com.mindhub.homebankig.models.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface LoanRepository extends JpaRepository <Loan, Long>{
    Loan findLoanById(Long id);
    boolean existsById(long id);
}