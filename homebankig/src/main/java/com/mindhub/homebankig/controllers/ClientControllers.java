package com.mindhub.homebankig.controllers;

import com.mindhub.homebankig.dtos.AccountDTO;
import com.mindhub.homebankig.dtos.ClientDTO;
import com.mindhub.homebankig.models.Account;
import com.mindhub.homebankig.models.Client;
import com.mindhub.homebankig.repositories.AccountRepository;
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
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @RequestMapping("/clients")//Lista de clientes
    public List<ClientDTO> getClients(){
        List<Client> listClient = clientRepository.findAll();
        List<ClientDTO> listClientDTO =
                listClient
                        .stream()
                        .map(client -> new ClientDTO(client))
                        .collect(Collectors.toList());

        return listClientDTO;
    }
    @RequestMapping("/admin") // lista de admins
    public List<ClientDTO> getAdmin(){
        List<Client> listAdmin = clientRepository.findAll();
        List<ClientDTO> listClientDTO =
                listAdmin
                        .stream()
                        .map(admin -> new ClientDTO(admin))
                        .collect(Collectors.toList());

        return listClientDTO;
    }
    @RequestMapping("/client/{id}")// Puede devolver una cuenta por cliente especifico si existe
    public ResponseEntity<Object>getClient(@PathVariable long id, Authentication authentication){
        Client client = clientRepository.findByEmail(authentication.getName());
        Account account = accountRepository.findById(id).orElse(null);
        if(account.getClient().equals(client)){
            AccountDTO accountDTO = new AccountDTO(account);
            return new ResponseEntity<>(accountDTO,HttpStatus.ACCEPTED);
        }else {
            return new ResponseEntity<>("The account is invalid", HttpStatus.I_AM_A_TEAPOT);
        }
    }
   @RequestMapping("/client/current")
   public ClientDTO getAll(Authentication authentication) {
       Client client = clientRepository.findByEmail(authentication.getName());
       return new ClientDTO(client);
   }
    @RequestMapping(path = "/clients", method = RequestMethod.POST)
    public ResponseEntity<Object> register(@RequestParam String firstName,
                                           @RequestParam String lastName,
                                           @RequestParam String email,
                                           @RequestParam String password) {
        if (firstName.isBlank() || lastName.isBlank() || email.isBlank() || password.isBlank()) {//utilizo isBlamk y no is Empty ya que verifica que tenga texto, no este vacio y que no tenga espacios en blanco.
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if (clientRepository.findByEmail(email) !=  null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }
        clientRepository.save(new Client(firstName, lastName, email, passwordEncoder.encode(password)));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}