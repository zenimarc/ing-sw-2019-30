package board;

import deck.AmmoCard;
import deck.Card;
import deck.Deck;
import deck.PowerCard;
import player.Player;
import weapon.WeaponCard;

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
    }

    public Board(int numskull, Billboard board, ArrayList<Card> ammoCard, ArrayList<Card> weaponCard, ArrayList<Card> powerUpCard) {
        this.numSkulls = numskull;
        this.playerSkulls = new HashMap<>();
        this.billboard = board;
        this.weaponDeck= new Deck(weaponCard);
        this.powerUpDeck= new Deck(powerUpCard);
        this.ammoDeck= new Deck(ammoCard);
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