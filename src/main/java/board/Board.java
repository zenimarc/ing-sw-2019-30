package board;

import deck.*;
import player.Player;
import powerup.PowerCardFactory;
import weapon.WeaponFactory;

import java.util.*;

/**
 * Board is a class which saves all information about the state of game
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


}