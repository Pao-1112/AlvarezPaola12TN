package com.mindhub.homebankig.repositories;

import com.mindhub.homebankig.dtos.AccountDTO;
import com.mindhub.homebankig.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RepositoryRestResource
public interface AccountRepository extends JpaRepository<Account,Long> {

    Account findByNumber(String number);
    boolean existsByNumber(String number);

}