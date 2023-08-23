package com.mindhub.homebankig.controllers;

import com.mindhub.homebankig.dtos.ClientDTO;
import com.mindhub.homebankig.models.Client;
import com.mindhub.homebankig.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ClientControllers {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
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
    @RequestMapping("/admin")
    public List<ClientDTO> getAdmin(){
        List<Client> listAdmin = clientRepository.findAll();
        List<ClientDTO> listClientDTO =
                listAdmin
                        .stream()
                        .map(admin -> new ClientDTO(admin))
                        .collect(Collectors.toList());

        return listClientDTO;
    }
    @RequestMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable long id){
        return new ClientDTO(clientRepository.findById(id).orElse(null));
    }
   @RequestMapping("/clients/current")
   public ClientDTO getAll(Authentication authentication){
        Client client = clientRepository.findByEmail(authentication.getName());
        return new ClientDTO(client);
    }
    @RequestMapping(path = "/clients", method = RequestMethod.POST)
    public ResponseEntity<Object> register(
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password) {
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if (clientRepository.findByEmail(email) !=  null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }
        clientRepository.save(new Client(firstName, lastName, email, passwordEncoder.encode(password)));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}