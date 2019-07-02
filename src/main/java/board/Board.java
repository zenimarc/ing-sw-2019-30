package board;

import board.billboard.Billboard;
import deck.*;
import org.jetbrains.annotations.NotNull;
import player.Player;
import powerup.PowerCard;
import powerup.PowerCardFactory;
import weapon.WeaponFactory;

import java.io.Serializable;
import java.util.*;

/**
 * Board is a class which saves all information about the state of game
 */
public class Board extends Observable implements Cloneable, Serializable {

    private int numSkulls;
    private Billboard billboard;
    private transient Deck weaponDeck;
    private transient Deck ammoDeck;
    private transient Deck powerUpDeck;
    private transient Deck ammoDiscardDeck;
    private transient Deck powerUpDiscardDeck;

    /**
     * Constructors
     */
    public Board(){
        this.numSkulls = 8;
    }

    public Board(int numskull) {
        this.numSkulls = numskull;
        this.billboard = new Billboard();
    }

    public Board(int numskull, Billboard board) {
        this.numSkulls = numskull;
        this.billboard = board;
        this.ammoDiscardDeck = new Deck();
        this.powerUpDiscardDeck = new Deck();

        this.powerUpDeck = new Deck(Card.powerCardsToCards((new PowerCardFactory().getPowerCardsList())));

        this.ammoDeck = new Deck(Card.ammoCardsToCards((new AmmoCardFactory()).getAmmoCardList()));
        setStartAmmoCard();

        this.weaponDeck = new Deck(Card.weponCardsToCards((new WeaponFactory()).getWeaponCardList()));
        setStartWeaponCard();

    }

    /**
     * This function sets the weapon cards in every RegenerationCell at the beginning of the game
     */
    private void setStartWeaponCard(){
        for(Cell c : billboard.getCellMap().keySet()){
            if(c.getClass() == RegenerationCell.class) {
                for (int i = 0; i < 3; i++) {
                    c.setCard(weaponDeck.draw());
                }
            }
        }
    }

    /**
     * This function sets the ammo cards in every NormalCell at the beginning of the game
     */
    private void setStartAmmoCard(){
        for(Cell c : billboard.getCellMap().keySet()){
            if(c.getClass() == NormalCell.class){
                Card card = ammoDeck.draw();
                addAmmoDiscardDeck((AmmoCard)card);
                c.setCard(card);
                    if (ammoDeck.getCards().get(0) == null)
                        ammoDeck.addAll(ammoDiscardDeck.getCards());
            }
        }
    }

    /**
     * This function clones the board
     * @return a clone of the board
     */
    public Board cloneBoard() {
        try{
            return (Board) this.clone();
        }catch (CloneNotSupportedException err){
             return null;
        }
    }

    /**
     * This function returns power up deck
     * @return the deck
     */
    public Deck getPowerUpDeck(){return this.powerUpDeck;}

    /**
     * This function returns weapon card deck
     * @return the deck
     */
    public Deck getWeaponCardDeck(){return this.weaponDeck;}

    /**
     * This function returns ammo card deck
     * @return the deck
     */
    public Deck getAmmoCardDeck(){return this.ammoDeck;}

    /**
     * This function returns discard power up card deck
     * @return the deck
     */
    public Deck getDiscardPowerUpDeck(){return this.powerUpDiscardDeck;}

    /**
     * This function returns discard ammo card deck
     * @return the deck
     */
    public Deck getDiscardAmmoCardDeck(){return this.ammoDiscardDeck;}

    /**
     * This function returns the billboard used
     * @return the billboard used
     */
    public Billboard getBillboard(){return billboard;}

    /**
     * This function returns the number of skulls still to be assigned
     * @return the remaining number of skulls
     */
    public int getSkulls(){return this.numSkulls;}

    /**
     * This function decrements the number of skulls in the board
     */
    public void decrementSkull(){
        this.numSkulls--;
    }

    /**
     * This function verifies if the final phase of the game can begin
     * @return true if the number of skulls is zero
     */
    public boolean isFinalFrenzy() {
        return (this.numSkulls == 0);
    }

    /**
     * Add AmmoCard to discard deck
     * @param ammoCard to discard
     */
    public void addAmmoDiscardDeck(AmmoCard ammoCard){
        this.ammoDiscardDeck.addCard(ammoCard);
}

    public  void addPowerUpDiscardDeck(PowerCard powerCard) {
        this.powerUpDiscardDeck.addCard(powerCard);
    }

    public void setPlayerCell(Player p, Cell c){
        p.setPawnCell(c);
        setChanged();
        notifyObservers(this.cloneBoard());
    }

    public Card giveCardFromCell(@NotNull Cell cell, Player player, int val){
        Card card = cell.giveCard(player, val);
        setChanged();
        notifyObservers(this.cloneBoard());
        return card;
    }

    public void addCardInCell(Card card, @NotNull Cell cell){
        cell.setCard(card);
        setChanged();
        notifyObservers(this.cloneBoard());
    }

    //TODO add addCardInCell(cell, card) sostituire metodo in playercontroller.grabweapon

    public Card giveCardFromPowerUpDeck(Player player){
        PowerCard pc = (PowerCard) powerUpDeck.draw();
        player.addPowerCard(pc);
        addPowerUpDiscardDeck(pc);
        if(powerUpDeck.getCards().isEmpty())
            powerUpDeck.addAll(powerUpDiscardDeck.getCards());
        return pc;
    }
}