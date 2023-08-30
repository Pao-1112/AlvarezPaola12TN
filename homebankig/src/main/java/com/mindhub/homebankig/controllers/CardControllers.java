package com.mindhub.homebankig.controllers;

import com.mindhub.homebankig.dtos.CardDTO;
import com.mindhub.homebankig.models.*;
import com.mindhub.homebankig.repositories.CardRepository;
import com.mindhub.homebankig.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
                        .collect(Collectors.toList());

        return listCardDTO;
    }
    @RequestMapping("/cards/{id}")
    public CardDTO getCards(@PathVariable long id){
        return new CardDTO(cardRepository.findById(id).orElse(null));
    }
    public int getRandomCardNumber(int min1, int max1){
        return (int)((Math.random() * (max1 - min1)) - min1);
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
    @RequestMapping(path = "/clients/current/cards", method = RequestMethod.POST)
    public ResponseEntity<Object> registerCard(Authentication authentication,
                                               @RequestParam CardType type,
                                               @RequestParam CardColor color){
        Client client = clientRepository.findByEmail(authentication.getName());
        Set<Card> cards = client.getCards();
        long devitCounter = cards.stream().filter(card -> card.getType() == CardType.DEBIT).count();
        long creditCounter = cards.stream().filter(card -> card.getType() == CardType.CREDIT).count();

        CardType cardType1 = CardType.DEBIT;
        CardType cardType2 = CardType.CREDIT;

        CardColor color1 = CardColor.GOLD;
        CardColor color2 = CardColor.SILVER;
        CardColor color3 = CardColor.TITANIUM;

        String cardNumber = getCardNumbers();
        int cvv = getRandomCvvNumber(100, 999);

        LocalDateTime thruDate = LocalDateTime.now();
        LocalDateTime fromDate = thruDate.plusYears(5);

        if(devitCounter >= 3 || creditCounter >= 3 ){
            return new ResponseEntity<>("403 forbidden", HttpStatus.FORBIDDEN);
        }else{
            if (color == color1 && type == cardType1) {
                Card card1;
                card1 = new Card(client.getFirstName() + " " + client.getLastName(), cardType1, color1, cardNumber, cvv, thruDate, fromDate);
                client.addCard(card1);
                cardRepository.save(card1);
                return new ResponseEntity<>("201 created", HttpStatus.CREATED);
            } if(color == color1 && type == cardType2){
                Card card2;
                card2 = new Card(client.getFirstName() + " " + client.getLastName(), cardType2, color1, cardNumber, cvv, thruDate, fromDate);
                client.addCard(card2);
                cardRepository.save(card2);
                return new ResponseEntity<>("201 created", HttpStatus.CREATED);
            } if(color == color2 && type == cardType1) {
                Card card3;
                card3 = new Card(client.getFirstName() + " " + client.getLastName(), cardType1, color2, cardNumber, cvv, thruDate, fromDate);
                client.addCard(card3);
                cardRepository.save(card3);
                return new ResponseEntity<>("201 created", HttpStatus.CREATED);
            } if(color == color2 && type == cardType2) {
                Card card4;
                card4 = new Card(client.getFirstName() + " " + client.getLastName(), cardType2, color2, cardNumber, cvv, thruDate, fromDate);
                client.addCard(card4);
                cardRepository.save(card4);
                return new ResponseEntity<>("201 created", HttpStatus.CREATED);
            } if(color == color3 && type == cardType1){
                Card card5;
                card5 = new Card(client.getFirstName() + " " + client.getLastName(), cardType1, color3, cardNumber, cvv, thruDate, fromDate);
                client.addCard(card5);
                cardRepository.save(card5);
                return new ResponseEntity<>("201 created", HttpStatus.CREATED);
            }else {
                Card card6;
                card6 = new Card(client.getFirstName() + " " + client.getLastName(), cardType2, color3, cardNumber, cvv, thruDate, fromDate);
                client.addCard(card6);
                cardRepository.save(card6);
                return new ResponseEntity<>("201 created", HttpStatus.CREATED);
            }
        }
    }
}