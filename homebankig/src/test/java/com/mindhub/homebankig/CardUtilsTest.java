package com.mindhub.homebankig;

import com.mindhub.homebankig.utils.CardUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
@SpringBootTest
public class CardUtilsTest {
    @Test
    public void cardNumberIsCreated(){
        String cardNumber = CardUtils.getCardNumbers();
        assertThat(cardNumber,is(not(emptyOrNullString())));

    }

    @Test
    public void cardNumberDifferent(){
        String cardNumber1 = CardUtils.getCardNumbers();
        String cardNumber2 = CardUtils.getCardNumbers();
        Assertions.assertNotEquals(cardNumber1, cardNumber2);
    }

    @Test
    public void cvvNumberCard(){
        int cvv = CardUtils.getRandomNumberCvvCard();
        assertThat(cvv, is(not(0)));
    }

    @Test
    public void cvvNumber(){
        int cvv1 = CardUtils.getRandomNumberCvvCard();
        Assertions.assertEquals(cvv1 >=100,cvv1 <= 999);
    }
}
