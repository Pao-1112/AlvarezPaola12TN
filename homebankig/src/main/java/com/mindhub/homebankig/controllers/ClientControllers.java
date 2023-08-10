package com.mindhub.homebankig.controllers;

import com.mindhub.homebankig.dtos.ClientDTO;
import com.mindhub.homebankig.models.Client;
import com.mindhub.homebankig.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ClientControllers {
    @Autowired
    private ClientRepository clientRepository;
    @RequestMapping("/clients")
    public List<ClientDTO> getClients(){
        List<Client> listClient = clientRepository.findAll();
        List<ClientDTO> listClientDTO =
                listClient
                        .stream()
                        .map(client -> new ClientDTO(client))
                        .collect(Collectors.toList());

        return listClientDTO;
    }

    @RequestMapping("/clients/{id}")//devuelve un solo cliente
    public ClientDTO getClients(@PathVariable long id){
        return new ClientDTO(clientRepository.findById(id).orElse(null));
    }
}
