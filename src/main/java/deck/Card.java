package deck;

import powerup.PowerCard;
import weapon.WeaponCard;

import java.io.Serializable;
import java.util.*;

/**
 * Card is an abstraction to manage the different type of cards
 */
public abstract class Card implements Serializable {

    /**
     * Default constructor
     */
    public Card() {
    }

    /**
     * This function convert a AmmoCard List into a Card one
     * @param ammoCards cards to be converted
     * @return a List of Card
     */
    public static List<Card> ammoCardsToCards(List<AmmoCard> ammoCards){
        return new ArrayList<>(ammoCards);
    }

    /**
     * This function convert a WeaponCard List into a Card one
     * @param weaponCards cards to be converted
     * @return a List of Card
     */
    public static List<Card> weponCardsToCards(List<WeaponCard> weaponCards){
        return new ArrayList<>(weaponCards);

    }

    /**
     * This function convert a PowerCard List into a Card one
     * @param powerCards cards to be converted
     * @return a List of Card
     */
    public static List<Card> powerCardsToCards(List<PowerCard> powerCards){
        return new ArrayList<>(powerCards);
    }

    /**
     * This function is implemented by sons
     */
    public String stringGUI() {
        return "";
    }
}