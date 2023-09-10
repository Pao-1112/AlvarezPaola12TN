package com.mindhub.homebankig.controllers;

import com.mindhub.homebankig.dtos.LoanAplicationDTO;
import com.mindhub.homebankig.dtos.LoanDTO;
import com.mindhub.homebankig.models.*;
import com.mindhub.homebankig.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class LoanControllers {
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @GetMapping("/loans")
    public List<LoanDTO> getLoans(){
        return loanRepository
                .findAll()
                .stream()
                .map(LoanDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    @RequestMapping(value = "/loans", method = RequestMethod.POST)
    public ResponseEntity<String> registerLoan(Authentication authentication, @RequestBody LoanAplicationDTO loanAplicationDTO) {

        if(authentication !=null){

            Client client = clientRepository.findByEmail(authentication.getName());
            Loan loan = loanRepository.findLoanById(loanAplicationDTO.getLoanId());
            Account toAccount = accountRepository.findByNumber(loanAplicationDTO.getToAccountNumber());

            //Alguno de los datos no es valido
            if(loanAplicationDTO.getAmount() <= 0.0){
                return new ResponseEntity<>("Some of the Amount is not valid",HttpStatus.FORBIDDEN);
            }
            if(loanAplicationDTO.getPayments() == 0){
                return new ResponseEntity<>("Some of the Payment is not valid",HttpStatus.FORBIDDEN);
            }
            if(loanAplicationDTO.getToAccountNumber() == null){
                return new ResponseEntity<>("Some of the Account number is not valid",HttpStatus.FORBIDDEN);
            }
            if(loanAplicationDTO.getLoanId() == 0){
                return new ResponseEntity<>("The loan does not exist",HttpStatus.FORBIDDEN);
            }
            //Si la cuenta de destino no existe
            if(toAccount == null){
                return new ResponseEntity<>("Some of the Account is not valid",HttpStatus.FORBIDDEN);
            }
            //La cuenta destino no pertenece al cliente autenticado
            Set<Account>clientAccounts = client.getAccounts();
            if(!clientAccounts.contains(toAccount)){
                return new ResponseEntity<>("If the destination account does not\n" +
                        "belongs to the authenticated client",HttpStatus.FORBIDDEN);
            }
            //Si el prestamo no existe
            if(!loanRepository.existsById((loanAplicationDTO.getLoanId()))){
                return new ResponseEntity<>("The loan does not exist",HttpStatus.FORBIDDEN);
            }
            //Si el monto solicitado supera el monto maximo permitido del prestamo solicitado
            if(loanAplicationDTO.getAmount() > loan.getMaxAmount()){
                return new ResponseEntity<>("The amount requested exceeds the maximum amount allowed for the requested loan",HttpStatus.FORBIDDEN);
            }
            //Si la cantidad de cuotas no está disponible para el prestamo solicitado
            //List<Integer> paymentsList = (List<Integer>) loan.getPayments();
            if(!loan.getPayments().contains(loanAplicationDTO.getPayments())){
                return new ResponseEntity<>("The amount of installments is not available for the requested loan",HttpStatus.FORBIDDEN);
            }

            double totalLoan = (loanAplicationDTO.getAmount() * 0.20) + loanAplicationDTO.getAmount(); //tasa de interes
            double amounts = Math.floor(totalLoan/loanAplicationDTO.getPayments());

            ClientLoan clientLoan = new ClientLoan(totalLoan, loanAplicationDTO.getPayments(), client, loan);
            loan.addClientLoan(clientLoan);
            client.addClientLoan(clientLoan);
            clientLoanRepository.save(clientLoan);

            String description = clientLoan.getLoan().getName() + " Loan Approved";

            Transaction transactionsLoanCredit = new Transaction (TransactionType.CREDIT, loanAplicationDTO.getAmount(), description, LocalDateTime.now(), toAccount);
            transactionRepository.save(transactionsLoanCredit);

            //Descuentos
            Double auxDestiny =loanAplicationDTO.getAmount() + toAccount.getBalance();

            //Actualización de montos
            toAccount.setBalance(auxDestiny);

            return new ResponseEntity<>("200 applied loan", HttpStatus.CREATED);

        } return new ResponseEntity<>("Unregistered customer",HttpStatus.FORBIDDEN);
    }
}
