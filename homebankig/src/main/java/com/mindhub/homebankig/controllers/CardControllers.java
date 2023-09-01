package com.mindhub.homebankig.controllers;

import com.mindhub.homebankig.dtos.CardDTO;
import com.mindhub.homebankig.models.*;
import com.mindhub.homebankig.repositories.CardRepository;
import com.mindhub.homebankig.repositories.ClientRepository;
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

    @RequestMapping("/cards")
    public List<CardDTO> getCard(){
        List<Card> listCard = cardRepository.findAll();
        List<CardDTO> listCardDTO =
                listCard
                        .stream()
                        .map(card -> new CardDTO(card))
                        .collect(toList());

        return listCardDTO;
    }
    @RequestMapping("/cards/{id}")
    public CardDTO getCards(@PathVariable long id){
        return new CardDTO(cardRepository.findById(id).orElse(null));
    }

    public int getRandomCardNumber(int min1, int max1){

        return (int)((Math.random() * (max1 - min1)) + min1);
    }

    public String getRandomCardNumber(){

        int cardRandomNumber = getRandomCardNumber(0001, 9999);

        return String.valueOf(cardRandomNumber);
    }

    public String getCardNumbers(){

        String cardNumber = "";

        for(int i = 0; i <= 4; i++){

            String numberSerie = getRandomCardNumber();

            cardNumber += ("-" + numberSerie);
        }
        return cardNumber.substring(1);
    }

    public int getRandomCvvNumber(int min2, int max2){
        return (int) ((Math.random() * (max2 - min2)) + min2);
    }

    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> registerCard(Authentication authentication,
                                               @RequestParam CardType cardType,
                                               @RequestParam CardColor cardColor){
        Client client = clientRepository.findByEmail(authentication.getName());
        Set<Card> cards = client.getCards();

        if (authentication != null){
            long devitCounter = cards.stream().filter(card -> card.getType() == CardType.DEBIT).count();
            long creditCounter = cards.stream().filter(card -> card.getType() == CardType.CREDIT).count();

            String cardNumber;

            do{
                cardNumber = getCardNumbers();
            }
            while(cardRepository.findByNumber(cardNumber).getExistsCard());

            if(devitCounter >= 3 || creditCounter >= 3){
                return new ResponseEntity<>("403 forbidden ", HttpStatus.FORBIDDEN);

            } else{
                if(!cards.stream().filter(card -> card.getType().equals(cardType)).filter(card -> card.getColor().equals(cardColor)).collect(toList()).isEmpty()){
                    return  new ResponseEntity<>("Option selected invalid, you alredy have a card of this type", HttpStatus.FORBIDDEN);
                }else {
                    int cvv = getRandomCvvNumber(100, 999);

                    Card card1 = new Card(client.getFirstName() + "" + client.getLastName(), cardType, cardColor, cardNumber, cvv,LocalDateTime.now().plusYears(5), LocalDateTime.now(), true);
                    client.addCard(card1);
                    cardRepository.save(card1);
                    clientRepository.save(client);

                    return new ResponseEntity<>("Card created successfull", HttpStatus.CREATED);
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}