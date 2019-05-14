package board;

import deck.*;
import player.Player;
import weapon.WeaponCard;
import weapon.WeaponFactory;

import java.util.*;

/**
 * Board is a class which //TODO Java doc
 */
public class Board {

    private int numSkulls;
    private HashMap<Player, Integer> playerSkulls;
    private Billboard billboard;
    private Deck weaponDeck;
    private Deck ammoDeck;
    private Deck powerUpDeck;
    private Deck ammoDiscardDeck;
    private Deck powerUpDiscardDeck;

    /**
     * Constructors
     */
    public Board(){
        this(8);
    }

    public Board(int numskull) {
        this.numSkulls = numskull;
        this.playerSkulls = new HashMap<>();
        this.billboard = new Billboard();
    }

    public Board(int numskull, Billboard board) {
        this.numSkulls = numskull;
        this.playerSkulls = new HashMap<>();
        this.billboard = board;
        this.powerUpDeck = new Deck();
        this.ammoDiscardDeck = new Deck();
        this.powerUpDiscardDeck = new Deck();

        this.ammoDeck = new Deck(Card.ammoCardsToCards((new AmmoCardFactory()).getAmmoCardList()));
        setStartAmmoCard();

        this.weaponDeck = new Deck(Card.weponCardsToCards((new WeaponFactory()).getWeaponCardList()));
        setStartWeaponCard();

    }

    private void setStartWeaponCard(){
        for(Cell c : billboard.getCellMap().keySet()){
            if(c.getClass() == RegenerationCell.class) {
                for (int i = 0; i < 3; i++) {
                    c.setCard(weaponDeck.draw());
                }
            }
        }
    }

    private void setStartAmmoCard(){
        for(Cell c : billboard.getCellMap().keySet()){
            if(c.getClass() == NormalCell.class){
                c.setCard(ammoDeck.draw());
            }
        }
    }

    /**
     * @return
     */
    public Board cloneBoard() {
        // TODO implement here
        return null;
    }

    /**
     * This function returns the power up deck
     * @return the deck
     */
    public Deck getPowerUpDeck(){return this.powerUpDeck;}

    /**
     * This function returns the weapon card deck
     * @return the deck
     */
    public Deck getWeaponCardDeck(){return this.weaponDeck;}

    /**
     * This function returns the ammo card deck
     * @return the deck
     */
    public Deck getAmmoCardDeck(){return this.ammoDeck;}

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


}