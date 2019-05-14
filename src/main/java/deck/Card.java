package deck;

import weapon.WeaponCard;

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

    public static List<Card> weponCardsToCards(List<WeaponCard> weaponCards){

        List<Card> cards = new ArrayList<>();
        for (WeaponCard weaponCard : weaponCards) {
            cards.add(weaponCard);
        }
        return cards;
    }

}