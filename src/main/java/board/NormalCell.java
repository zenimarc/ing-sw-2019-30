package board;

import deck.AmmoCard;
import deck.Card;
import deck.Color;

import java.util.*;

/**
 * A NormalCell is a cell which can have an ammoCard
 */

public class NormalCell extends Cell {

    private AmmoCard ammoCard;

    /**
     * Constructors
     */

    public NormalCell(){
        this(null,null);
    }

    public NormalCell(Color color){
        this(color, null);
    }

    public NormalCell(Color color, AmmoCard ammoCard) {
        super(color);
        this.ammoCard = ammoCard;
    }

    /**
     * This function returns the ammoCard of the cell
     * @param p
     * @return
     */
    @Override
    public Card getCard(int p) { //TODO problema se ritorna NULL
        return ammoCard;
    }

    /**
     * This function removes the ammoCard from the cell if it exists
     * @param card the card in the cell
     * @return null because there is no more a card
     */
    @Override
    public Card removeCard(Card card) {
        if(ammoCard==card){
            ammoCard = null;
            return card;
        }
        return null;
    }

    public Card removeCard(){//TODO non capisco a cosa serva
        return removeCard(ammoCard);
    }

    /**
     * This function verifies if there is already an ammoCard and, in case, it sets a new one if there is none
     * @param card the card to bet set
     * @return true if there was not an ammoCard, else false
     */
    @Override
    public boolean setCard(Card card) {
        if(card.getClass()==AmmoCard.class && ammoCard==null){
            this.ammoCard = (AmmoCard) card;
            return true;
        }
        return false;
    }
}