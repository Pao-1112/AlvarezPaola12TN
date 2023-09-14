package com.mindhub.homebankig.utils;

public class AccountUtils {
    public static int getRandomAccountNumber(int min, int max){
        return (int) ((Math.random() * (max - min) + min));
    }
}
