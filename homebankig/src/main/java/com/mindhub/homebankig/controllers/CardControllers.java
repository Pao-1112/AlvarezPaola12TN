package com.mindhub.homebankig.controllers;

import com.mindhub.homebankig.dtos.CardDTO;
import com.mindhub.homebankig.models.*;
import com.mindhub.homebankig.repositories.CardRepository;
import com.mindhub.homebankig.repositories.ClientRepository;
import com.mindhub.homebankig.utils.CardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("api")
public class CardControllers {

    @Autowired
    CardRepository cardRepository;

    @Autowired
    ClientRepository clientRepository;

    @GetMapping("/clients/current/cards")
    public List<CardDTO> getCardsCurrent(Authentication authentication){
        Client client = clientRepository.findByEmail(authentication.getName());
        List<CardDTO> currentCards = client.getCards().stream().map(CardDTO::new).collect(toList());
        return currentCards;
    }

    /*public int getRandomCardNumber(int min1, int max1){ return (int)((Math.random() * (max1 - min1)) + min1); }

    public String getRandomNumberCard(){

        int cardRandomNumber = getRandomCardNumber(0001, 9999);

        return String.valueOf(cardRandomNumber);
    }

    public String getCardNumbers(){

        String cardNumber = "";

        for(int i = 0; i <= 4; i++){

            String numberSerie = getRandomNumberCard();

            cardNumber += ("-" + numberSerie);
        }
        return cardNumber.substring(1);
    }

    public int getRandomCvvNumber(int min2, int max2){
        return (int) ((Math.random() * (max2 - min2)) + min2);
    }*/

    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> registerCard(Authentication authentication,
                                               @RequestParam CardType cardType,
                                               @RequestParam CardColor cardColor){
        if (authentication != null){
            Client client = clientRepository.findByEmail(authentication.getName());
            Set<Card> cards = client.getCards();
            long devitCounter = cards.stream().filter(card -> card.getType() == CardType.DEBIT).count();
            long creditCounter = cards.stream().filter(card -> card.getType() == CardType.CREDIT).count();

            if (devitCounter >= 3 || creditCounter >= 3) {
                return new ResponseEntity<>("403 forbidden", HttpStatus.FORBIDDEN);
            } else {
                if (!cards.stream().filter(card -> card.getType().equals(cardType)).filter(card -> card.getColor().equals(cardColor)).collect(toList()).isEmpty()) {
                    return new ResponseEntity<>("Option selected invalid, you already have a card of this type.", HttpStatus.FORBIDDEN);
                } else {

                    String cardNumber;
                    cardNumber = CardUtils.getCardNumbers();

                    if(cards.stream().filter(card -> card.getNumber().equals(cardNumber)).collect(toList()).isEmpty()){

                        Integer cvv1 = CardUtils.getRandomNumberCvvCard();

                        if (cards.stream().filter(card -> card.getCvv().equals(cvv1)).collect(toList()).isEmpty()) {

                            Card card = new Card(client.getFirstName() + " " + client.getLastName(), cardType, cardColor, cardNumber, cvv1, LocalDateTime.now().plusYears(5), LocalDateTime.now());

                            client.addCard(card);
                            cardRepository.save(card);
                            return new ResponseEntity<>("Card created successfully", HttpStatus.CREATED);
                        }
                    }
                    //Creo un numero de tarjeta y verifico que no exita en la base de datos
                    //do{
                        //cardNumber = CardUtils.getCardNumbers();

                   // } while (cardRepository.existsByNumber(cardNumber));
                    //  Puedo utilizar ciclos para consultar de en vez de lo contenido
                    //  en el while (para no ir a la base de datos):
                    //if(!cards.stream().filter(card -> card.getNumber().equals(number1)).collect(toList()).isEmpty());

                    //Creo un cvv y verifico que no exista en la base de datos
                    //int cvv1;

                    //do{
                        //cvv1 = CardUtils.getRandomNumberCvvCard();

                    //}while (cardRepository.existsByCvv(cvv1));

                    //Card card = new Card(client.getFirstName() + " " + client.getLastName(), cardType, cardColor, cardNumber , cvv1, LocalDateTime.now().plusYears(5), LocalDateTime.now());

                    //client.addCard(card);
                    //cardRepository.save(card);
                    //return new ResponseEntity<>("Card created successfully",HttpStatus.CREATED);
                }
            }return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}