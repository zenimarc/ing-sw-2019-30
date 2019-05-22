package board;

import deck.AmmoCard;
import deck.Card;
import constants.Color;
import player.Player;

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
      //  this(Color.values()[new Random().nextInt(3)],new AmmoCard());
    }

    public NormalCell(Color color){
        this(color, new AmmoCard());
    }

    public NormalCell(Color color, AmmoCard ammoCard) {
        super(color);
        this.ammoCard = ammoCard;
    }

    /**
     * This function returns the ammoCard of the cell
     * @param p not used
     * @return an AmmoCard
     */
    @Override
    public Card getCard(int p) { //TODO problema se ritorna NULL
        return this.ammoCard;
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

    public Card removeCard(){
        return removeCard(ammoCard);
    }

    /**
     * This set an AmmoCard in this cell
     * @param card the card to be set
     * @return true if there was not an ammoCard, else false
     */
    @Override
    public boolean setCard(Card card) {
        if(card.getClass()==AmmoCard.class){
            this.ammoCard = (AmmoCard) card;
            return true;
        }
        return false;
    }

    /**
     * This function adds ammo from the ammo card to a player and, in case, it gives a power card
     * @param player to receive a card
     * @param i not used
     */
    @Override
    public Card giveCard(Player player, int i){
        AmmoCard ac = this.ammoCard;
        player.addAmmo(ac.getAmmo());

        this.ammoCard = new AmmoCard();

        return ac;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("NrmCell\t");
        if(ammoCard!= null) stringBuilder.append(ammoCard.toString());
        else  stringBuilder.append("no-AmmoCard");

        return stringBuilder.toString();
    }
}