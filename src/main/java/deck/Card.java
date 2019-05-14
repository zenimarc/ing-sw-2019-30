package deck;

import java.util.*;

/**
 * Card is an abstraction to manage the different type of cards
 */
public abstract class Card {

    /**
     * Default constructor
     */
    public Card() {
    }


    public static List<Card> ammoCardsToCards(List<AmmoCard> ammoCards){

        List<Card> cards = new ArrayList<>();
        for (AmmoCard ammoCard : ammoCards) {
            cards.add(ammoCard);
        }
        return cards;
    }

}