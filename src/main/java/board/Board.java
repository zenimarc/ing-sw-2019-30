package board;

import player.Player;

import java.util.*;

/**
 * The
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

    /**
     * End Constructors
     */

    /**
     * @return
     */
    public Board cloneBoard() {
        // TODO implement here
        return null;
    }

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