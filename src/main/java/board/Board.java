package board;

import player.Player;

import java.util.*;

/**
 * Board is a class which //TODO Java doc
 */
public class Board {

    private int numSkulls;
    private HashMap<Player, Integer> playerSkulls;
    private Billboard billboard;


    /**
     * Constructors
     */
    public Board(){
        this(8);
    }

    public Board(int numskull) {
        this.numSkulls = numskull;
        this.playerSkulls = new HashMap<>(); //TODO controllare questa parte come dovrebbe servire
        this.billboard = new Billboard();
    }

    public Board(int numskull, Billboard board) {
        this.numSkulls = numskull;
        this.playerSkulls = new HashMap<>(); //TODO controllare questa parte come dovrebbe servire
        this.billboard = board;
    }

    /**
     * @return
     */
    public Board cloneBoard() {
        // TODO implement here
        return null;
    }

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