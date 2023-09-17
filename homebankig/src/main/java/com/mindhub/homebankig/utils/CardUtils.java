package com.mindhub.homebankig.utils;

public class CardUtils {
    public static int getRandomCardNumber(int min1, int max1){ return (int)((Math.random() * (max1 - min1)) + min1); }

    public static String getRandomNumberCard(){

        int cardRandomNumber = getRandomCardNumber(0001, 9999);

        return String.valueOf(cardRandomNumber);
    }

    public static String getCardNumbers(){

        String cardNumber1 = "";

        for(int i = 0; i <= 3; i++){

            String numberSerie = getRandomNumberCard();

            cardNumber1 += ("-" + numberSerie);
        }
        return cardNumber1.substring(1);
    }

    public static int getRandomCvvNumber(int min2, int max2){
        return (int) ((Math.random() * (max2 - min2)) + min2);
    }

    public static int getRandomNumberCvvCard(){

        return getRandomCvvNumber(100,999);
    }
}
