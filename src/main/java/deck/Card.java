package deck;

import powerup.PowerCard;
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

    /**
     * Cast List of WeaponCard in List of Card
     * @param weaponCards
     * @return
     */
    public static List<Card> weponCardsToCards(List<WeaponCard> weaponCards){
        List<Card> cards = new ArrayList<>();
        for (WeaponCard weaponCard : weaponCards) {
            cards.add(weaponCard);
        }
        return cards;
    }

    /**
     * Cast List of PowerCard in List of Card
     * @param powerCards
     * @return
     */
    public static List<Card> powerCardsToCards(List<PowerCard> powerCards){
        List<Card> cards = new ArrayList<>();
        for (PowerCard powerCard : powerCards) {
            cards.add(powerCard);
        }
        return cards;

    }


    public String stringGUI() {
        return "";
    }
}