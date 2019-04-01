package board;

import deck.AmmoCard;
import deck.Card;
import deck.Color;

import java.util.*;

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
     * End Cosnstructors
     */

    //WARNING: Potrebbe tornare null;
    @Override
    public Card getCard() {
        return ammoCard;
    }

    @Override
    public Card removeCard(Card card) {
        if(ammoCard==card){
            ammoCard = null;
            return card;
        }
        return null;
    }

    public Card removeCard(){
        return removeCard(ammoCard);
    }

    @Override
    public boolean setCard(Card card) {
        if(card.getClass()==AmmoCard.class && ammoCard==null){
            this.ammoCard = (AmmoCard) card;
            return true;
        }
        return false;
    }
}