package com.mindhub.homebankig.controllers;

import com.mindhub.homebankig.dtos.ClientDTO;
import com.mindhub.homebankig.dtos.LoanAplicationDTO;
import com.mindhub.homebankig.dtos.LoanDTO;
import com.mindhub.homebankig.dtos.TransactionDTO;
import com.mindhub.homebankig.models.*;
import com.mindhub.homebankig.repositories.*;
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
    private LoanRepository loanRepository;
    private AccountRepository accountRepository;
    private ClientRepository clientRepository;
    private TransactionRepository transactionRepository;
    private ClientLoanRepository clientLoanRepository;


    @Transactional
    @RequestMapping(value = "/loans", method = RequestMethod.POST)
    public ResponseEntity<String> registerLoan(Authentication authentication,@RequestParam String accountNumber, @RequestBody LoanAplicationDTO loanAplicationDTO, @RequestBody Loan loan){

        Client client = clientRepository.findByEmail(authentication.getName());
        Loan loanType = loanRepository.findByName(loanAplicationDTO.getLoanTypeId());
        Account account = accountRepository.findByNumber(accountNumber);

        Account targetAccount = accountRepository.findByNumber(account.getNumber());
        Account sourceAccount = accountRepository.findByNumber(account.getNumber());


        if(authentication != null){

            //Alguno de los datos no es valido
            if(loanAplicationDTO == null || loanAplicationDTO.getAmount() < 0.0 || loanType == null || loanAplicationDTO.getPayments() == 0 || loanAplicationDTO.getToAccountNumber() == null || loanAplicationDTO.getAmount() == 0 || loanAplicationDTO.getLoanTypeId() == null){
                return new ResponseEntity<>("403 some of the data is not valid",HttpStatus.FORBIDDEN);
            }
            //Si la cuenta de destino no existe
            if(targetAccount == null){
                return new ResponseEntity<>("403 some of the data is not valid",HttpStatus.FORBIDDEN);
            }

            //La cuenta destino no pertenece al cliente autenticado
            Set<Account> clientAccounts = client.getAccounts();
            if(clientAccounts.contains(account)){
                return new ResponseEntity<>("403 if the destination account does not\n" +
                        "belongs to the authenticated client",HttpStatus.FORBIDDEN);
            }

            //Si el prestamo no existe
            if(loanType == null){
                return new ResponseEntity<>("403 the loan does not exist",HttpStatus.FORBIDDEN);
            }

            //Si el monto solicitado supera el monto maximo permitido del prestamo solicitado
            if(loanAplicationDTO.getAmount() > loanType.getMaxAmount() || loanAplicationDTO.getAmount() <= 0){
                return new ResponseEntity<>("403 the amount requested exceeds the maximum amount allowed for the requested loan",HttpStatus.FORBIDDEN);
            }

            //Si la cantidad de cuotas no está disponible para el prestamo solicitado
            List<Integer> paymentsList = (List<Integer>) loan.getPayments();
            if(!paymentsList.contains(loanAplicationDTO.getPayments())){
                return new ResponseEntity<>("403 the amount of installments is not available for the requested loan",HttpStatus.FORBIDDEN);
            }



            double totalLoans = (loanAplicationDTO.getAmount() * 0.20) + loanAplicationDTO.getAmount();
            double payments = Math.floor(totalLoans/loanAplicationDTO.getPayments());


            ClientLoan clientLoan = new ClientLoan(loanAplicationDTO.getAmount(), loanAplicationDTO.getPayments(), client, loanType);
            Transaction transactionsLoanCredit = new Transaction (TransactionType.CREDIT, loanAplicationDTO.getAmount(), loanType.getName(), LocalDateTime.now(), account);

            transactionRepository.save(transactionsLoanCredit);
            clientLoanRepository.save(clientLoan);

            //Descuentos
            Double auxDestiny =loanAplicationDTO.getAmount() + account.getBalance();
            Double auxOrigin = loanAplicationDTO.getAmount() - account.getBalance();

            //Actualización de montos
            targetAccount.setBalance(auxDestiny);
            sourceAccount.setBalance(auxOrigin);

            return new ResponseEntity<>("200 applied loan", HttpStatus.CREATED);

        } return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
