package board;

import board.Cell;
import constants.Constants;
import deck.Card;
import constants.Color;
import player.Player;
import weapon.WeaponCard;

import java.util.*;

/**
 * A RegenerationCell is a cell which can have WeaponCards and where players can respawn if killed
 */

public class RegenerationCell extends Cell {

    private WeaponCard[] weaponCard;

    /**
     * Constructor
     */

    public RegenerationCell() {
        this(null, new WeaponCard[]{null,null,null});
    }

    public RegenerationCell(Color color) {
        this(color, new WeaponCard[]{null,null,null});
    }

    public RegenerationCell(Color color, WeaponCard[] weaponCard){
        super(color);

        this.weaponCard = new WeaponCard[Constants.MAX_WEAPON_REGENERATIONCELL.getValue()];

        for(int i=0;i<this.weaponCard.length && i<weaponCard.length;i++){
            this.weaponCard[i] = weaponCard[i];
        }
    }

    /**
     * This function returns the list of cards in the RegenerationCell
     * @return the list of cards set in the cell
     */
    public ArrayList<WeaponCard> getCards() {
        return new ArrayList<>(Arrays.asList(weaponCard));
    }

    /**
     * This function returns the card in a precise position
     * @param p the position of the card wanted
     * @return the card wanted if there is one, else null
     */
    @Override
    public Card getCard(int p) {
        if(p>=3){return null;}
        return weaponCard[p];
    }

    /**
     * This function adds cards in the RegenerationCell
     * @param card the card to be added
     * @return true if a card was added, false if not possible
     */
    @Override
    public boolean setCard(Card card) {
        //check card is a weaponCard
        //if(card.getClass() != WeaponCard.class){return false;} TODO: getClass prende la classe a runtime, mentre WeaponCard.class è statica

        //if weaponCard[] has a free slot => insert card
        for(int i=0; i<weaponCard.length;i++){
            if(weaponCard[i]==null){
                weaponCard[i] = (WeaponCard) card;
                return true;
            }
        }
        return false;
    }

    /**
     * This adds 3 cards in the RegenerationCell at the beginning of the game //TODO serve davvero il for ?
     * @param weaponCards to be added
     */
    public void setCards(WeaponCard[] weaponCards){
        for(int i=0;i<3;i++){
            this.weaponCard = weaponCards;
        }
    }

    /**
     * This function removes a card which was picked
     * @param card the card to be picked
     * @return a card if there is one, else null
     */
    @Override
    public Card removeCard(Card card) {
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

    /**
     * This function gives the i weapon to a player
     * @param player to receive a card
     * @param i card to pick
     */
    @Override
    public Card giveCard(Player player, int i){
        WeaponCard wc = this.weaponCard[i];
        player.addWeapon(wc);
        this.weaponCard[i] = null;
        return wc;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("RegCell\t");
        stringBuilder.append("WeapCard: ");
        stringBuilder.append(weaponCard[0]);
        stringBuilder.append("(1) ");
        stringBuilder.append(weaponCard[1]);
        stringBuilder.append("(2) ");
        stringBuilder.append(weaponCard[2]);
        stringBuilder.append("(3) ");

        return stringBuilder.toString();
    }

}