package board;

import constants.Constants;
import deck.Card;
import deck.Color;
import deck.WeaponCard;

import java.util.*;

/**
 * 
 */
public class RegenerationCell extends Cell {

    private WeaponCard[] weaponCard;

    /**
     * Constructor
     */

    public RegenerationCell() {
        this(null, new WeaponCard[3]);
    }

    public RegenerationCell(Color color) {
        this(color, new WeaponCard[3]);
    }

    public RegenerationCell(Color color, WeaponCard[] weaponCard){
        super(color);

        this.weaponCard = new WeaponCard[Constants.MAX_WEAPON_REGENERATIONCELL.getValue()];

        for(int i=0;i<this.weaponCard.length && i<weaponCard.length;i++){
            this.weaponCard[i] = weaponCard[i];
        }
    }

    /**
     * End constructor
     */


    public ArrayList<WeaponCard> getCards() {
        return new ArrayList<WeaponCard>(Arrays.asList(weaponCard));
    }

    @Override
    public boolean setCard(Card card) {
        //check card is a weaponCard
        if(card.getClass() != WeaponCard.class){return false;}

        //if weaponCard[] has a free slot => insert card
        for(int i=0; i<weaponCard.length;i++){
            if(weaponCard[i]==null){
                weaponCard[i] = (WeaponCard) card;
                return true;
            }
        }
        return false;
    }

    @Override
    public Card removeCard(Card card) {
        //Check card is WeaponCard
        if(card.getClass()!=WeaponCard.class){return null;}
        WeaponCard wc = (WeaponCard) card;

        //if card in weaponCards => remove its
        for (int i=0;i<weaponCard.length;i++){
            if(weaponCard[i]==wc){
                weaponCard[i]=null;
                return wc;
            }
        }

        return null;
    }

}