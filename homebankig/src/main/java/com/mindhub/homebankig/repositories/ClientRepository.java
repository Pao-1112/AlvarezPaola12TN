package com.mindhub.homebankig.repositories;

import com.mindhub.homebankig.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource //ignoro el controlador haciendo peticiones http por medio de @RepositoryRestResource
public interface ClientRepository extends JpaRepository<Client,Long> {
    Client findByEmail(String email);
}